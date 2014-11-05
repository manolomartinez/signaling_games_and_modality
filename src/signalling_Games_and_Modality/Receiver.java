/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

/**
 * @author manolo
 *
 */
public class Receiver {

	
	private ContinuousSpace<Object> space;
    private Grid<Object> grid;
    private float energy;
    private float[][] strategy;
    private boolean busy;

    public Sender(ContinuousSpace<Object> space, Grid<Object> grid,
    		float energy, float[][] strategy) {
        this.space = space;
        this.grid = grid;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = busy;
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
					if (!receiver.busy()) {
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
    				if (!monster.busy()) {
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
        		engage(closestReceiver, closestMonster);
        		
    	}
    	}
    }
    
    public boolean busy() {
    	return busy;
    }
   
    

}
