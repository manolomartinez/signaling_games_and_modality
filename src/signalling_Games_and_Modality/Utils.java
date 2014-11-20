package signalling_Games_and_Modality;

import repast.simphony.random.RandomHelper;

public class Utils {

	
	final static int numberOfStates = 3;
	final static int numberOfMessages = 3;
	final static int numberOfActs = 3;
	
	final static double[][] senderPureStrats =
			identityMatrix(numberOfStates, numberOfMessages);
	
	final static double[][] receiverPureStrats =
			identityMatrix(numberOfMessages, numberOfActs);
	
	final static double[][] identityMatrix(int rows, int columns) {
		double[][] iM;
		iM = new double[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (i == j) {
					iM[i][j] = 1;
				}
				else {
					iM[i][j] = 0;
				}
			}		
		}
		return iM;
	}
	
	final static double[][] randomPureSenderStrat() {
		double[][] rPSS;
		rPSS = new double[numberOfStates][numberOfMessages];
		for (int i = 0; i < numberOfStates; i++) {
			rPSS[i] = senderPureStrats[
			                           RandomHelper.nextIntFromTo
			                           (0, numberOfMessages - 1)];
		}
		return rPSS;
	}
	
	final static double[][] randomPureReceiverStrat() {
		double[][] rPSS;
		rPSS = new double[numberOfMessages][numberOfActs];
		for (int i = 0; i < numberOfMessages; i++) {
			rPSS[i] = receiverPureStrats[
			                           RandomHelper.nextIntFromTo(
			                        		   0, numberOfActs - 1)];
		}
		return rPSS;
	}
	
	final static double[] randomReceiverInvestmentPolicy() {
		double airInv = RandomHelper.nextDoubleFromTo(0, 1);
		double seaInv = RandomHelper.nextDoubleFromTo(0, 1);
		double zeroInv = RandomHelper.nextDoubleFromTo(0, 1);
		double total = airInv + seaInv + zeroInv;
		double[] rRIP = {airInv/total, seaInv/total, zeroInv/total};
		return rRIP;
	}
	
	final static double[][] initialSenderStrat =
		{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
	
	final static double[][] initialReceiverStrat = 
			/* Probability of air attack,
			probability of sea attack,
			probability of investment */
		{{0, 0, 1},
			{1, 0, 0},
			{0, 1, 0}};
	
	final static double[] initialReceiverInvestmentPolicy = 
			/* Proportion of air investment,
			 * proportion of sea investment,
			 * proportion of "zero investment" */
		{0.3, 0.3, 0.4};

	final static double maxInvestment = 2;
	
	final static double payoffInvestment(double investment) {
		return (20 - 10/Math.exp(investment));
	}
	
	public static int weightedRandomChoice(double[] probVector) {
		// Choose an index randomly with weights given by probVector
		int randomIndex = -1;
		double random = Math.random();
		for (int i = 0; i < probVector.length; ++i)
		{
		    random -= probVector[i];
		    if (random <= 0.0d)
		    {
		        randomIndex = i;
		        break;
		    }
		}
		return randomIndex;
	}
}
