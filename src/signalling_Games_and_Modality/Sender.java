/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.engine.schedule.ScheduledMethod;
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
    private float energy;
    private float[][] strategy;
    private boolean busy;
    private Receiver receiverEngaged;
    private Monster monsterEngaged;

    public Sender(ContinuousSpace<Object> space, Grid<Object> grid,
    		Network<Object> network, float energy, float[][] strategy) {
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
    		MooreQuery<Object> nearbyObjectsQuery = 
    				new MooreQuery(grid, this, 1, 1);
    		// find the closest idle monster and receiver in nearbyObjects
    		double receiverMinDistance = Double.POSITIVE_INFINITY;
    		double monsterMinDistance = Double.POSITIVE_INFINITY;
    		Receiver closestReceiver = null;
    		Monster closestMonster = null;
    		for (Object obj : nearbyObjectsQuery.query()) {
				if (obj instanceof Receiver) {
					Receiver receiver = (Receiver)obj;
					if (!network.getEdges(receiver)
							.iterator().hasNext()) { // i.e., if the receiver is not busy
	    				double receiverNewDistance =
	    						space.getDistance(senderLocation, 
	    								space.getLocation(receiver));
	    				if (receiverNewDistance < receiverMinDistance) {
	    					closestReceiver = receiver;
	    					receiverMinDistance = receiverNewDistance;
	    				}
					}
    			else if (obj instanceof Monster) {
    				Monster monster = (Monster)obj;
    				if (!network.getEdges(monster)
							.iterator().hasNext()) { // i.e., if the monster is not busy
	    				double monsterNewDistance =
	    						space.getDistance(senderLocation, 
	    								space.getLocation(obj));
	    				if (monsterNewDistance < monsterMinDistance) {
	    					closestMonster = (Monster) obj;
	    					monsterMinDistance = monsterNewDistance;
	    				}		  		
    				}
				}
				}
    		}
    		
    		if (closestReceiver != null && closestMonster != null) {
        		network.addEdge(this, closestReceiver);
        		network.addEdge(this, closestMonster);
        		network.addEdge(closestReceiver, closestMonster);
        		
    	}
    	}
    }
 
    
    public boolean busy() {
    	return busy;
    }
   
    
}
