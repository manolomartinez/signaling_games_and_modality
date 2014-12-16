package signalling_Games_and_Modality;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.context.Context;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;

public class MinAirInvestment implements AggregateDataSource {

	@Override
	public String getId() {
		return "MinAirInvestment";
	}

	@Override
	public Class<?> getDataType() {
		return DoubleMatrix1D.class;
	}

	@Override
	public Class<?> getSourceType() {
		return Context.class;
	}

	@Override
	public Object get(Iterable<?> objs, int size) {
		Context context = (Context) objs.iterator().next();
		IndexedIterable<Receiver> receivers = context.getObjects(Receiver.class);
		return minAir(receivers);
	}

	@Override
	public void reset() {		
	}
	
	private double minAir(IndexedIterable<Receiver> receivers) {
		double runningMinAir = 1;
		for (Receiver receiver : receivers) {
			double airInv = receiver.investmentPolicy().get(0);
			if (airInv < runningMinAir) {
				runningMinAir = airInv;
			}
		}
		return runningMinAir;
	}
}
