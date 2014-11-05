package signalling_Games_and_Modality;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class MonsterStyle extends DefaultStyleOGL2D {
	
	@Override 
	public Color getColor(Object agent) {
		if (((Monster) agent).type()==0) {
			return Color.YELLOW;
		}
		else if (((Monster) agent).type()==1) {
			return Color.BLUE;
		}
		else {
			return Color.red;
		}
	}	
	
	@Override 
	public float getScale(Object agent) {
		return 2;
	}
}