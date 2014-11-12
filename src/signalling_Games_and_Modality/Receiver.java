/**
 * 
 */
package signalling_Games_and_Modality;

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
    private double[][] strategy;
    private double[] investmentPolicy;
    private boolean busy;
    private boolean ready;

    public Receiver(ContinuousSpace<Object> space, Grid<Object> grid, 
    		Network<Object> network,
    		double energy, double[][] strategy, double[] investmentPolicy) {
        this.space = space;
        this.grid = grid;
        this.energy = energy;
        this.strategy = strategy;
        this.investmentPolicy = investmentPolicy;
        this.network = network;
        this.busy = false;
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
		Context<Object> context = ContextUtils.getContext(this);
		context.add(hunt);
    }
    
    
    public boolean busy() {
    	this.busy = network.getDegree(this) > 0;
    	return this.busy;
    }
    
    public double[][] strategy() {
    	return this.strategy;
    }
    
    public double[] investmentPolicy() {
    	return this.investmentPolicy;
    }
    
    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	timePasses();
    }
    
    public void timePasses() {
    	energy--;
    	if (energy == 0) {
    		die();
    	}
    }
    
    public void addEnergy(double payoff) {
    	this.energy += payoff;
    }
    
    public void relocate() {
    	Context<Object> context = ContextUtils.getContext(this);
    	context.remove(this);
    	context.add(this);
    	NdPoint pt = space.getLocation(this);
		grid.moveTo(this, (int)pt.getX(), (int)pt.getY());
    }
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	context.remove(this);
    }
}
