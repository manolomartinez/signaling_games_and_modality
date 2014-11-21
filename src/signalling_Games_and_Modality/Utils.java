package signalling_Games_and_Modality;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.random.RandomHelper;

public class Utils {

	
	final static int numberOfStates = 3;
	final static int numberOfMessages = 3;
	final static int numberOfActs = 3;
	
	final static DoubleMatrix2D senderPureStrats =
			identityMatrix(numberOfStates, numberOfMessages);
	
	final static DoubleMatrix2D receiverPureStrats =
			identityMatrix(numberOfMessages, numberOfActs);
	
	final static DoubleMatrix2D identityMatrix(int rows, int columns) {
		DoubleMatrix2D iM;
		iM = new SparseDoubleMatrix2D(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (i == j) {
					iM.set(i, j, 1);
				}
				else {
					iM.set(i, j, 0);
				}
			}		
		}
		return iM;
	}
	
	final static DoubleMatrix2D randomPureSenderStrat() {
		DoubleMatrix2D rPSS;
		rPSS = new SparseDoubleMatrix2D(numberOfStates, numberOfMessages);
		for (int i = 0; i < numberOfStates; i++) {
			DoubleMatrix1D randomRow = 
					senderPureStrats.viewRow(RandomHelper.nextIntFromTo
                    (0, numberOfMessages - 1));
			for (int j = 0; j < numberOfMessages; j++) {
				rPSS.set(i, j, randomRow.get(j));
			}
		}
		return rPSS;
	}
	
	final static DoubleMatrix2D randomPureReceiverStrat() {
		DoubleMatrix2D rPRS;
		rPRS = new SparseDoubleMatrix2D(numberOfMessages, numberOfActs);
		for (int i = 0; i < numberOfMessages; i++) {
			DoubleMatrix1D randomRow = 
					receiverPureStrats.viewRow(RandomHelper.nextIntFromTo
                    (0, numberOfActs - 1));
			for (int j = 0; j < numberOfActs; j++) {
				rPRS.set(i, j, randomRow.get(j));
			}
		}
		return rPRS;
	}
	
	final static DoubleMatrix1D randomReceiverInvestmentPolicy() {
		DoubleMatrix1D investment = DoubleFactory1D.dense.random(3);
		double total = investment.zSum();
		investment.assign(Functions.div(total));
		return investment;
	}
	
	static double[][] initialSenderStratArray =
		{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
	
	final static DoubleMatrix2D initialSenderStrat =
		new SparseDoubleMatrix2D(initialSenderStratArray);
	
	static double[][] initialReceiverStratArray = 
		{{0, 0, 1},	{1, 0, 0},{0, 1, 0}};
		
	final static DoubleMatrix2D initialReceiverStrat = 
			/* Probability of air attack,
			probability of sea attack,
			probability of investment */
			new SparseDoubleMatrix2D(initialReceiverStratArray);
	
	static double[] initialReceiverInvestmentPolicyArray =
		{0.3, 0.3, 0.4};
	
	final static DoubleMatrix1D initialReceiverInvestmentPolicy = 
			/* Proportion of air investment,
			 * proportion of sea investment,
			 * proportion of "zero investment" */
		new DenseDoubleMatrix1D(initialReceiverInvestmentPolicyArray);

	final static double maxInvestment = 20;
	
	final static double payoffInvestment(double investment) {
		return (20 - 10/Math.exp(investment));
	}
	
	public static int weightedRandomChoice(DoubleMatrix1D probVector) {
		// Choose an index randomly with weights given by probVector
		int randomIndex = -1;
		double random = Math.random();
		for (int i = 0; i < probVector.size(); ++i)
		{
		    random -= probVector.get(i);
		    if (random <= 0.0d)
		    {
		        randomIndex = i;
		        break;
		    }
		}
		return randomIndex;
	}
}
