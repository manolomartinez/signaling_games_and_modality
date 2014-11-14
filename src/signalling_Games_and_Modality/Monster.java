/**
 * 
 */
package signalling_Games_and_Modality;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
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
public class Monster {
    
    private ContinuousSpace<Object> space;
    private Grid<Object> grid;
    private Network<Object> network;
    private int type; // 0: Undecided, 1: Air; 2: Sea
    private int life;
    private boolean busy;
    private Hunt myHunt;

    public Monster(ContinuousSpace<Object> space, Grid<Object> grid,
    		Network<Object> network, int type, int life){
        this.space = space;
        this.grid = grid;
        this.type = type;
        this.life = life;
        this.busy = false;
        this.setMyHunt(null);
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
    	if (this.myHunt instanceof Hunt) {
    		context.remove(this.myHunt);
    	}
    	context.remove(this);
		Monster monster = new Monster(space, grid, network, 0, 0);
		context.add(monster);
		NdPoint pt = space.getLocation(monster);
		grid.moveTo(monster, (int)pt.getX(), (int)pt.getY());
    }
    
    public int type() {
    	return type;
    }
    
    public boolean busy() {
    	return busy;
    }

	public Hunt getMyHunt() {
		return myHunt;
	}

	public void setMyHunt(Hunt myHunt) {
		this.myHunt = myHunt;
	}
}
