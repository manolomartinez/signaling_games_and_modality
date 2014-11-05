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

    public Receiver(ContinuousSpace<Object> space, Grid<Object> grid,
    		float energy, float[][] strategy) {
        this.space = space;
        this.grid = grid;
        this.energy = energy;
        this.strategy = strategy;
        this.busy = busy;
    }

    @Watch(watcheeClassName = "signalling_Games_and_Modality.Sender",
    		watcheeFieldNames = "busy",
    		query = "within_Moore 1 [grid]",
    		whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
        		
  
    
    public boolean busy() {
    	return busy;
    }
   
    

}
