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
	private Network<Object> network;

	
	private double unpreparedAttack(int type, int receiverAct) {
		// return the payoff for a receiverAct attack 
		// to monsters of type
		if (monster.type() == 0) {
			return 1; // the payoff for hunting an undecided
		}
		if (type + 1 == receiverAct) { // if type and attack match
			return 10;
		}
		else {
			return 0;
		}
	}

	public Hunt(Network<Object> network, Sender sender, Receiver receiver, Monster monster) {
		this.sender = sender;
		this.receiver = receiver;
		this.monster = monster;
		this.network = network;
		this.stage = 0;
	}
		
		@ScheduledMethod(start = 1, interval = 1)
		public void step() {
			switch (stage) {
			case 0: firstRound();
					break;
			case 1: // System.out.print("We have a stage 1\n");
					secondRound();
					break;
			}
		}
	
		private void firstRound() {
			// System.out.print("A hunt starts\n");
			DoubleMatrix1D senderStrat =
					this.sender.strategy().viewRow(this.monster.type());
			int senderMessage = Utils.weightedRandomChoice(senderStrat);
			DoubleMatrix1D receiverStrat =
					this.receiver.strategy().viewRow(senderMessage);
			// System.out.print(this.monster.type());
			int receiverAct = Utils.weightedRandomChoice(receiverStrat);
			if (receiverAct < 2 || monster.type() > 0) { // No second round
				double payoff = unpreparedAttack(this.monster.type(), receiverAct);
				this.sender.addEnergy(payoff);
				this.receiver.addEnergy(payoff);
				dismantle();
				monster.die();
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
			if (monster.type() > 0) { // if monster has evolved
				DoubleMatrix1D senderStrat =
						this.sender.strategy().viewRow(this.monster.type());
				int senderMessage = Utils.weightedRandomChoice(senderStrat);
				DoubleMatrix1D receiverStrat =
						this.receiver.strategy().viewRow(senderMessage);
				int receiverAct = Utils.weightedRandomChoice(receiverStrat);
				double payoff = preparedAttack(this.monster.type(), receiverAct);
				this.sender.addEnergy(payoff);
				this.receiver.addEnergy(payoff);
				dismantle();
				monster.die();
				// System.out.print("A hunt ends\n");
				Context<Object> context = ContextUtils.getContext(this);
		    	context.remove(this);
				
			}
		}

		private double preparedAttack(int type, int receiverAct) {
			// return the payoff for a receiverAct attack 
			// to monsters of type
			
			double probInvestment = 
					this.receiver.investmentPolicy().get(0) + 
					this.receiver.investmentPolicy().get(1);

			double investment = Utils.maxInvestment * probInvestment;
			
			if (type - 1 == receiverAct) { // if type and attack match
				return Utils.payoffInvestment(
						this.receiver.investmentPolicy().get(receiverAct)
								* Utils.maxInvestment) - investment;
			}
			else {
				return -1 * investment;
			}
		}
		
		private void dismantle() {
			RepastEdge<Object> edgeRS = network.getEdge(receiver, sender);
			RepastEdge<Object> edgeSM = network.getEdge(sender, monster);
			network.removeEdge(edgeRS);
			network.removeEdge(edgeSM);
			sender.setMyHunt(null);
			monster.setMyHunt(null);
			receiver.setMyHunt(null);
			sender.relocate();
			receiver.relocate();
		}
}
