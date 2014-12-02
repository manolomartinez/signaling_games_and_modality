/**
 * 
 */
package signalling_Games_and_Modality;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * @author manolo
 *
 */
public class Receiver {

	
	private ContinuousSpace<Object> space;
    private double energy;
    private DoubleMatrix2D strategy;
    private DoubleMatrix1D investmentPolicy;
    private boolean busy;
    private Hunt myHunt;
    private Monster myMonster;
    private Sender mySender;

    public Receiver(ContinuousSpace<Object> space, 
    		double energy, DoubleMatrix2D strategy,
    		DoubleMatrix1D investmentPolicy) {
        this.space = space;
        this.energy = energy;
        this.strategy = strategy;
        this.investmentPolicy = investmentPolicy;
        this.busy = false;
        this.setMyHunt(null);
        this.setMyMonster(null);
        this.setMySender(null);
    }
    
    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	findSender();
    	timePasses();
    }
    
    private void findSender() {
    	// If you are not busy, find a close enough Sender with Monster
    	// System.out.print(String.format("I'm at %s\n", senderLocation));
    	if (this.getMySender() == null) {
        	NdPoint receiverLocation = space.getLocation(this);
    		// get objects in the Receiver's 10x10 Moore neighborhood
    		ContinuousWithin<Object> nearbyQuery = 
    				new ContinuousWithin<Object>(space, this, 20);
    		Iterable<Object> nearbyList =
    				nearbyQuery.query();
    		Sender closestSender = null;
    		for (Object obj : nearbyList) {
    			if (obj instanceof Sender) {
    				// System.out.print(obj.toString());
    				if (((Sender) obj).getMyMonster() != null && ((Sender) obj).getMyReceiver() == null) {
    					// i.e., if the sender has a monster but no receiver
	    				closestSender = (Sender) obj;
	    				break;
	    			}		  		
    			}
    		}
    		if (closestSender != null) {
    			// System.out.print("We have a Monster\n");
        		this.setMySender(closestSender);
        		closestSender.setMyReceiver(this);
        		this.setMyMonster(closestSender.getMyMonster());
        		Hunt hunt = new Hunt(this.mySender, this, this.myMonster);
        		this.mySender.setMyHunt(hunt);
        		this.myMonster.setMyHunt(hunt);
        		this.setMyHunt(hunt);
        		Context<Object> context = ContextUtils.getContext(this);
        		context.add(hunt);
        		}
    		else {
    				// System.out.print("No Monsters!\n");
    		}
    		}
    	// else {
    	//	System.out.print("Busy!\n");
    	// }
    }
    
    public DoubleMatrix2D strategy() {
    	return this.strategy;
    }
    
    public String prettyStrategy() {
    	return this.strategy.toString();
    }
    
    public DoubleMatrix1D investmentPolicy() {
    	return this.investmentPolicy;
    }
    
    public String prettyInvestmentPolicy() {
    	return this.investmentPolicy.toString();
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
    
    public void addEnergy(double payoff) {
    	// System.out.print(String.format("payoff: %f\n", payoff));
    	this.energy += payoff;
    }
    
    public double getEnergy() {
    	return this.energy;
    }
    
    public void relocate() {
    	double newX = RandomHelper.nextDoubleFromTo(0, 50);
    	double newY = RandomHelper.nextDoubleFromTo(0, 50);
    	System.out.println("Relocate receiver!");
    	space.moveTo(this, newX, newY);
    }
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	if (this.mySender != null) {
    		this.mySender.setMyReceiver(null);
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
    	Receiver receiver;
    	if (mutationProb < 0.9) {
    		receiver = new Receiver(space, energy * .45,
				this.strategy ,this.investmentPolicy);
    	}
    	else {
    		receiver = new Receiver(space, energy * .45,
    				Utils.perturb(this.strategy) ,
    				Utils.perturb(this.investmentPolicy));
    	}
		context.add(receiver);
		this.energy = this.energy * .5;
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
	
	public Sender getMySender() {
		return this.mySender;
	}

	public void setMySender(Sender mySender) {
		this.mySender = mySender;
	}
	
}