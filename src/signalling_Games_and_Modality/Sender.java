/**
 * 
 */
package signalling_Games_and_Modality;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cern.colt.matrix.DoubleMatrix2D;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author manolo
 *
 */
public class Sender {
	
	private ContinuousSpace<Object> space;
    private double energy;
    private DoubleMatrix2D strategy;
    private boolean busy;
    private boolean goodForReceivers;
	private Hunt myHunt;
	private Monster myMonster;
	private Receiver myReceiver;

    public Sender(ContinuousSpace<Object> space,
    		double energy, DoubleMatrix2D strategy) {
        this.space = space;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = false;
        this.goodForReceivers = false;
        this.setMyMonster(null);
        this.setMyHunt(null);
        this.setMyReceiver(null);
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	goodForReceivers();
    	findMonster();
    	timePasses();
    }
    
    
    public boolean goodForReceivers() {
    	this.goodForReceivers = this.getMyMonster() != null &&
    			this.getMyReceiver() == null;
    	// if (gfR) {
    	//	System.out.print("Good");
    	//}
    	//else {
    	//	System.out.print("Bad");
    	//}
    	return this.goodForReceivers;	
    }
    
    private void findMonster() {
    	// If you are not busy, find a close enough idle Monster
    	// System.out.print(String.format("I'm at %s\n", senderLocation));
    	if (this.getMyMonster() == null) {
        	NdPoint senderLocation = space.getLocation(this);
    		// get objects in the Sender's 10x10 Moore neighborhood
    		ContinuousWithin<Object> nearbyQuery = 
    				new ContinuousWithin<Object>(space, this, 20);
    		Iterable<Object> nearbyList =
    				nearbyQuery.query();
    		Monster closestMonster = null;
    		for (Object obj : nearbyList) {
    			if (obj instanceof Monster) {
    				// System.out.print(obj.toString());
    				if (((Monster) obj).getMySender() == null) { // i.e., if the monster is not busy
	    				closestMonster = (Monster)obj;
	    				break;
	    			}		  		
    			}
    		}
    		if (closestMonster != null) {
    			// System.out.print("We have a Monster\n");
        		this.setMyMonster(closestMonster);
        		closestMonster.setMySender(this);
        		this.busy = true;
        		}
    		else {
    				// System.out.print("No Monsters!\n");
    		}
    		}
    	// else {
    	//	System.out.print("Busy!\n");
    	// }
    }
    
    public void timePasses() {
    	energy -= 1;
    	if (energy <= 0) {
    		die();
    	}
    	if (energy > Utils.reproducingEnergy) {
    		reproduce();
    	}
    }
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	if (this.myReceiver != null) {
    		this.myReceiver.setMySender(null);
    	}
    	if (this.myMonster != null) {
    		this.myMonster.setMySender(null);
    	}
    	if (this.myHunt != null) {
    		context.remove(this.myHunt);
    		this.myHunt.dismantle();
    	}
    	context.remove(this);
    }
    
    public void reproduce() {
    	Context<Object> context = ContextUtils.getContext(this);
    	double mutationProb = RandomHelper.nextDoubleFromTo(0, 1);
    	Sender offspring;
    	if (mutationProb < 0.9) {
    		offspring =
    			new Sender(space, this.energy * .5, this.strategy);
    	}
    	else {
    		offspring =
    			new Sender(space, this.energy * .5, Utils.perturb(this.strategy));
    	}
		context.add(offspring);
		this.energy = this.energy *.5;
    }
    
    public DoubleMatrix2D strategy() {
    	return this.strategy;
    }
    
    public String prettyStrategy() {
    	return this.strategy.toString();
    }
    
    public void addEnergy(double payoff) {
    	// System.out.print(String.format("payoff: %f\n", payoff));
    	this.energy += payoff;
    }
    
    public double getEnergy() {
    	return this.energy;
    }
    
    public void relocate() {
    	if (ContextUtils.getContext(this) == null) {
    	}
    	else{
	    	double newX = RandomHelper.nextDoubleFromTo(0, 50);
	    	double newY = RandomHelper.nextDoubleFromTo(0, 50);
	    	/*System.out.println("Relocate sender!");*/
	    	space.moveTo(this, newX, newY);
    	}
    }
    
    @Override
	public String toString() {
		// Override default Java implementation just to have a nicer
		// representation
		return String.format("Sender @ location %s", space.getLocation(this));
	}

	public Hunt getMyHunt() {
		return this.myHunt;
	}

	public void setMyHunt(Hunt myHunt) {
		this.myHunt = myHunt;
	}  
	
	public Monster getMyMonster() {
		return this.myMonster;
	}

	public void setMyMonster(Monster myMonster) {
		this.myMonster = myMonster;
	} 
	
	public Receiver getMyReceiver() {
		return this.myReceiver;
	}

	public void setMyReceiver(Receiver myReceiver) {
		this.myReceiver = myReceiver;
	} 
}
