/**
 * 
 */
package signalling_Games_and_Modality;

import java.util.Collection;
import java.util.Iterator;

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
    private double energy;
    private double[][] strategy;
    private boolean busy;

    public Sender(ContinuousSpace<Object> space, Grid<Object> grid,
    		Network<Object> network, double energy, double[][] strategy) {
        this.space = space;
        this.grid = grid;
        this.network = network;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = false;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	NdPoint senderLocation = space.getLocation(this);
    	System.out.print(String.format("I'm at %s\n", senderLocation));
    	if (!this.busy) {
    		// get objects in the Sender's 10x10 Moore neighborhood
    		MooreQuery<Object> nearbyQuery = 
    				new MooreQuery(grid, this, 10, 10);
    		Iterable<Object> nearbyIterable =
    				nearbyQuery.query();
    		// find the closest idle monster in nearbyMostersIterator
    		double monsterMinDistance = Double.POSITIVE_INFINITY;
    		Monster closestMonster = null;
    		for (Object obj : nearbyIterable) {
    			if (obj instanceof Monster) {
    				if (network.getDegree(obj) == 0) { // i.e., if the monster is not busy
	    				double monsterNewDistance =
	    						space.getDistance(senderLocation, 
	    								space.getLocation(obj));
	    				if (monsterNewDistance < monsterMinDistance) {
	    					closestMonster = (Monster)obj;
	    					monsterMinDistance = monsterNewDistance;
	    				}		  		
    				}
    			}
    		}
    		if (closestMonster != null) {
    			System.out.print("We have a Monster\n");
        		network.addEdge(this, closestMonster);
        		this.busy = true;
        		}
    		else {
    			System.out.print("No Monsters!\n");
    		}
    		}
    	else {
    		System.out.print("Busy!\n");
    	}
    }
    
    public boolean goodForReceivers() {
    	boolean gfR = network.getDegree(this) == 1;
    	if (gfR) {
    		System.out.print("Good");
    	}
    	else {
    		System.out.print("Bad");
    	}
    	return gfR;
    	
    }
    
    public boolean busy() {
    	this.busy = network.getDegree(this) > 0;
    	return this.busy;
    }
    
    @Override
	public String toString() {
		// Override default Java implementation just to have a nicer
		// representation
		return String.format("Sender @ location %s", grid.getLocation(this));
	}  
}
