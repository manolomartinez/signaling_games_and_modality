/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;

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
    private boolean busy;

    public Receiver(ContinuousSpace<Object> space, Grid<Object> grid, 
    		Network<Object> network,
    		double energy, double[][] strategy) {
        this.space = space;
        this.grid = grid;
        this.energy = energy;
        this.strategy = strategy;
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
    	System.out.print(sender.toString());
    	System.out.print(String.format("Receiver @ location %s", grid.getLocation(this)));
    	System.out.print("\n");
		network.addEdge(this, sender); 
    }
        		
    public boolean busy() {
    	this.busy = network.getDegree(this) > 0;
    	return this.busy;
    }
}
