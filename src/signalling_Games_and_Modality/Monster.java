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
    private int type; // 0: Undecided, 1: Air; 2: Sea
    private int life;
    private boolean busy;
    private Hunt myHunt;
    private Sender mySender;

    public Monster(ContinuousSpace<Object> space, int type, int life){
        this.space = space;
        this.type = type;
        this.life = life;
        this.busy = false;
        this.setMyHunt(null);
        this.setMySender(null);
    }

    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
    	life++;
    	if (life >= 2) {
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
    	if (this.myHunt != null) {
    		context.remove(this.myHunt);
    	}
    	// Spawn a "new" Monster
    	context.remove(this);
    	Monster monster = new Monster(space, 0, 0);
    	context.add(monster);
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
	
	public Sender getMySender() {
		return mySender;
	}

	public void setMySender(Sender mySender) {
		this.mySender = mySender;
	}
	
	@Override
	public String toString() {
		// Override default Java implementation just to have a nicer
		// representation
		return String.format("Monster @ location %s", space.getLocation(this));
	}
}
