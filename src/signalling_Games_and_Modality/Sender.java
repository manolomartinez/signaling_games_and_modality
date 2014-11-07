/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;

/**
 * @author manolo
 *
 */
public class Sender {
	
	private ContinuousSpace<Object> space;
    private Grid<Object> grid;
    private Network<Object> network;
    private double energy;
    private double[][] strategy;
    private boolean busy;
    private Receiver receiverEngaged;
    private Monster monsterEngaged;

    public Sender(ContinuousSpace<Object> space, Grid<Object> grid,
    		Network<Object> network, double energy, double[][] strategy) {
        this.space = space;
        this.grid = grid;
        this.network = network;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = busy;
        this.receiverEngaged = null;
        this.monsterEngaged = null;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	NdPoint senderLocation = space.getLocation(this);
    	if (!busy) {
    		// get objects in the Sender's 1x1 Moore neighborhood
    		MooreQuery<Monster> nearbyObjectsQuery = 
    				new MooreQuery(grid, this, 1, 1);
    		// find the closest idle monster in nearbyObjects
    		double monsterMinDistance = Double.POSITIVE_INFINITY;
    		Monster closestMonster = null;
    		for (Monster monster : nearbyObjectsQuery.query()) {
    				if (!network.getEdges(monster)
							.iterator().hasNext()) { // i.e., if the monster is not busy
	    				double monsterNewDistance =
	    						space.getDistance(senderLocation, 
	    								space.getLocation(monster));
	    				if (monsterNewDistance < monsterMinDistance) {
	    					closestMonster = monster;
	    					monsterMinDistance = monsterNewDistance;
	    				}		  		
				}
			}
    		
    		if (closestMonster != null) {
        		network.addEdge(this, closestMonster); 
        		this.busy = true;
    	}
    	}
    }
    
    public boolean busy() {
    	return busy;
    }
   
    
}
