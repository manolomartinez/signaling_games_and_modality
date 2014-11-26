/**
 * 
 */
package signalling_Games_and_Modality;

import java.util.Collection;
import java.util.Iterator;

import cern.colt.matrix.DoubleMatrix2D;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * @author manolo
 *
 */
public class Sender {
	
	private ContinuousSpace<Object> space;
    private Grid<Object> grid;
    private Network<Object> network;
    private double energy;
    private DoubleMatrix2D strategy;
    private boolean busy;
	private Hunt myHunt;

    public Sender(ContinuousSpace<Object> space, Grid<Object> grid,
    		Network<Object> network, double energy, DoubleMatrix2D strategy) {
        this.space = space;
        this.grid = grid;
        this.network = network;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = false;
        this.setMyHunt(null);
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	findMonster();
    	timePasses();
    }
    
    private void findMonster() {
    	// If you are not busy, find the closest idle Monster
    	// System.out.print(String.format("I'm at %s\n", senderLocation));
    	if (!this.busy()) {
        	NdPoint senderLocation = space.getLocation(this);
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
    			// System.out.print("We have a Monster\n");
        		network.addEdge(this, closestMonster);
        		this.busy = true;
        		}
    		// else {
    		//		System.out.print("No Monsters!\n");
    		//}
    		}
    	// else {
    	//	System.out.print("Busy!\n");
    	// }
    }
    
    public boolean goodForReceivers() {
    	boolean gfR = network.getDegree(this) == 1 &&
    			network.getOutDegree(this) == 1;
    	// if (gfR) {
    	//	System.out.print("Good");
    	//}
    	//else {
    	//	System.out.print("Bad");
    	//}
    	return gfR;
    	
    }
    
    public boolean busy() {
    	this.busy = network.getDegree(this) > 0;
    	return this.busy;
    }
    
    public void timePasses() {
    	energy--;
    	if (energy <= 0) {
    		die();
    	}
    }
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	if (this.myHunt instanceof Hunt) {
    		context.remove(this.myHunt);
    	}
    	context.remove(this);
    }
    
    public DoubleMatrix2D strategy() {
    	return this.strategy;
    }
    
    public void addEnergy(double payoff) {
    	this.energy += payoff;
    }
    
    public double getEnergy() {
    	return this.energy;
    }
    
    public void relocate() {
    	Context<Object> context = ContextUtils.getContext(this);
    	double newX = RandomHelper.nextDoubleFromTo(0, 50);
    	double newY = RandomHelper.nextDoubleFromTo(0, 50);
    	space.moveTo(this, newX, newY);
		grid.moveTo(this, (int)newX, (int)newY);
    }
    
    @Override
	public String toString() {
		// Override default Java implementation just to have a nicer
		// representation
		return String.format("Sender @ location %s", grid.getLocation(this));
	}

	public Hunt getMyHunt() {
		return myHunt;
	}

	public void setMyHunt(Hunt myHunt) {
		this.myHunt = myHunt;
	} 
	
	public String prettyStrategy() {
		return strategy.toString();
	}
}
