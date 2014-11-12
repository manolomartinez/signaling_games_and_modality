package signalling_Games_and_Modality;

public class Utils {
	final static double[][] initialSenderStrat =
		{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}};
	
	final static double[][] initialReceiverStrat = 
			/* Probability of air attack,
			probability of sea attack,
			probability of investment */
		{{0.2, 0.2, 0.6},
			{0.2, 0.2, 0.6},
			{0.2, 0.2, 0.6}};
	
	final static double[] initialReceiverInvestmentPolicy = 
			/* Proportion of air investment,
			 * proportion of sea investment,
			 * proportion of "zero investment" */
		{0.3, 0.3, 0.4};

	final static double maxInvestment = 20;
	
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
