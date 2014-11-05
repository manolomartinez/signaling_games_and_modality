/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.engine.schedule.ScheduledMethod;

/**
 * @author manolo
 *
 */
public class Monster {
    
    private ContinuousSpace<Object> space;
    private Grid<Object> grid;
    private int type; // 0: Undecided, 1: Air; 2: Sea
    private int life;
    private boolean busy;
    private Sender senderEngaged;
    private Receiver receiverEngaged;

    public Monster(ContinuousSpace<Object> space, Grid<Object> grid,
    		int type, int life){
        this.space = space;
        this.grid = grid;
        this.type = type;
        this.life = life;
        this.busy = false;
        this.senderEngaged = null;
        this.receiverEngaged = null;
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	life++;
    	if (life >= 10) {
    		if (type == 0) {
    			type = RandomHelper.nextIntFromTo(1, 2);
    			life = 0;
    		} else {
    			die();
    		}
    	} 
    }
    
    public void die() {
    	Context<Object> context = ContextUtils.getContext(this);
    	context.remove(this);
		Monster monster = new Monster(space, grid, 0, 0);
		context.add(monster);
    }
    
    public int type() {
    	return type;
    }
    
    public boolean busy() {
    	return busy;
    }
    	
    public void engage(Sender sender, Receiver receiver) {
    	this.senderEngaged = sender;
    	this.receiverEngaged = receiver;
    	this.busy = true;
    }
}
