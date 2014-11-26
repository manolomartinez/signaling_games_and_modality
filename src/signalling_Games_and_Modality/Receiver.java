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
    private Grid<Object> grid;
    private Network<Object> network;
    private double energy;
    private DoubleMatrix2D strategy;
    private DoubleMatrix1D investmentPolicy;
    private boolean busy;
    private boolean ready;
    private Hunt myHunt;

    public Receiver(ContinuousSpace<Object> space, Grid<Object> grid, 
    		Network<Object> network,
    		double energy, DoubleMatrix2D strategy,
    		DoubleMatrix1D investmentPolicy) {
        this.space = space;
        this.grid = grid;
        this.energy = energy;
        this.strategy = strategy;
        this.investmentPolicy = investmentPolicy;
        this.network = network;
        this.busy = false;
        this.setMyHunt(null);
    }

    @Watch(watcheeClassName = "signalling_Games_and_Modality.Sender",
    		watcheeFieldNames = "busy",
    		query = "within_moore 10",
    		whenToTrigger = WatcherTriggerSchedule.IMMEDIATE,
    		triggerCondition = "$watchee.goodForReceivers() && !$watcher.busy()",
    		pick = 1)
    public void engage(Sender sender) {
    	// System.out.print(sender.toString());
    	// System.out.print(String.format("Receiver @ location %s", grid.getLocation(this)));
    	// System.out.print("\n");
		network.addEdge(this, sender);
		busy();
		Monster myMonster = (Monster) network.getSuccessors(sender).iterator().next();
		Hunt hunt = new Hunt(network, sender, this, myMonster);
		sender.setMyHunt(hunt);
		myMonster.setMyHunt(hunt);
		this.setMyHunt(hunt);
		Context<Object> context = ContextUtils.getContext(this);
		context.add(hunt);
    }
    
    
    public boolean busy() {
    	this.busy = network.getDegree(this) > 0;
    	return this.busy;
    }
    
    public DoubleMatrix2D strategy() {
    	return this.strategy;
    }
    
    public DoubleMatrix1D investmentPolicy() {
    	return this.investmentPolicy;
    }
    
    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	timePasses();
    }
    

    public void timePasses() {
    	energy -= .2;
    	if (energy <= 0) {
    		die();
    	}
    	if (energy > 50) {
    		reproduce();
    	}
    }
    
    public void reproduce() {
    	Context<Object> context = ContextUtils.getContext(this);
		Receiver receiver = new Receiver(space, grid, network, energy * .45,
				this.strategy ,this.investmentPolicy);
		context.add(receiver);
		this.energy = this.energy * .5;
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
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	if (this.myHunt instanceof Hunt) {
    		context.remove(this.myHunt);
    	}
    	context.remove(this);
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
	
	public String prettyInvestmentPolicy() {
		return investmentPolicy.toString();
	}
}
