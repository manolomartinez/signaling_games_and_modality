package signalling_Games_and_Modality;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

public class Information {
	final static double[][] fromConditionalToJoint(double[] unconds, double[][] conds) {
		DenseDoubleMatrix2D matrix = new DenseDoubleMatrix2D;
	    /*
	    Take a matrix of conditional probabilities of the column random variable on
	    the row r. v., and a vector of unconditional probabilities of the row r. v.
	    and output a matrix of joint probabilities.

	    Input: 
	            [[P(B1|A1), ...., P(Bn|A1)],..., [P(B1|Am),...,P(Bn|Am)]]
	            [P(A1), ..., P(Am)]
	    Output: 
	            [[P(B1,A1), ...., P(Bn,A1)],..., [P(B1,Am),...,P(Bn,Am)]]
	    
	    return conds * unconds[..., None]
	    */
	}
	
	private double[] scalarDotVector (double scalar, double[] vector) {
		double[] result;
		result = new double[vector.length];
		for (int i = 0; i < vector.length; i++) {
			result[i] = scalar * vector[i];
		}
		return result;
	}
}



}
