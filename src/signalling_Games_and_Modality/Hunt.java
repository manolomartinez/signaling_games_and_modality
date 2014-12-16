package signalling_Games_and_Modality;

import cern.colt.matrix.DoubleMatrix1D;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.util.ContextUtils;

public class Hunt {
	
	private int stage;
	private Sender sender;
	private Receiver receiver;
	private Monster monster;
	
	
	public Hunt(Sender sender, Receiver receiver, Monster monster) {
		this.sender = sender;
		this.receiver = receiver;
		this.monster = monster;
		this.stage = 0;
	}
	
	
		@ScheduledMethod(start = 1, interval = 1)
		public void step() {
			if (sender == null || receiver == null || monster == null) {
				dismantle();
				Context<Object> context = ContextUtils.getContext(this);
		    	context.remove(this);
		    	return;
			}
			// System.out.println(String.format("%d", monster.type()));
			switch (stage) {
			case 0: firstRound();
					break;
			case 1: // System.out.print("We have a stage 1\n");
					secondRound();
					break;
			}
		}
	
		private void firstRound() {
			/*System.out.println("A hunt starts");*/
			DoubleMatrix1D senderStrat =
					this.sender.strategy().viewRow(this.monster.type());
			int senderMessage = Utils.weightedRandomChoice(senderStrat);
			DoubleMatrix1D receiverStrat =
					this.receiver.strategy().viewRow(senderMessage);
			// System.out.print(this.monster.type());
			int receiverAct = Utils.weightedRandomChoice(receiverStrat);
			if (receiverAct < 2 || this.monster.type() > 0) { // No second round
				double payoff = unpreparedAttack(this.monster.type(), receiverAct);
				// System.out.print(String.format("payoff: %d\n", payoff));
				this.sender.addEnergy(payoff);
				this.receiver.addEnergy(payoff);
				dismantle();
				this.monster.die();
				// System.out.print("A hunt ends in the 1st round.\n");
				Context<Object> context = ContextUtils.getContext(this);
		    	context.remove(this);
			}
			else {
				stage++;
			}
		}
		
		private void secondRound() {
			// System.out.print("Second round!\n");
			if (this.monster.type() > 0) { // if monster has evolved
				DoubleMatrix1D senderStrat =
						this.sender.strategy().viewRow(this.monster.type());
				int senderMessage = Utils.weightedRandomChoice(senderStrat);
				DoubleMatrix1D receiverStrat =
						this.receiver.strategy().viewRow(senderMessage);
				int receiverAct = Utils.weightedRandomChoice(receiverStrat);
				double payoff = preparedAttack(this.monster.type(), receiverAct);
				// System.out.print(String.format("payoff: %d\n", payoff));
				this.sender.addEnergy(payoff);
				this.receiver.addEnergy(payoff);
				dismantle();
				this.monster.die();
				// System.out.print("A hunt ends\n");
				Context<Object> context = ContextUtils.getContext(this);
		    	context.remove(this);
				
			}
		}

		private double unpreparedAttack(int type, int receiverAct) {
			// return the payoff for a receiverAct attack 
			// to monsters of type
			if (monster.type() == 0) {
				return 0; // the payoff for hunting an undecided. If it is positive, quick hunters predominate
			}
			if (type - 1 == receiverAct) { // if type and attack match
				return 10;
			}
			else {
				return 0;
			}
		}

		private double preparedAttack(int type, int receiverAct) {
			// return the payoff for a receiverAct attack 
			// to monsters of type
			
			if (type - 1 == receiverAct) { // if type and attack match
				return Utils.payoffInvestment(
						Utils.maxInvestment * this.receiver.investmentPolicy().get(receiverAct)) - 
						Utils.maxInvestment;
			}
			else {
				return -1 * Utils.maxInvestment;
			}
		}
		
		void dismantle() {
			this.receiver.setMySender(null);
			this.sender.setMyReceiver(null);
			this.sender.setMyMonster(null);
			this.monster.setMySender(null);
			this.sender.setMyHunt(null);
			this.monster.setMyHunt(null);
			this.receiver.setMyHunt(null);
			this.sender.relocate();
			this.receiver.relocate();
		}
}
