package signalling_Games_and_Modality;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.context.Context;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;

public class MeanReceiverInvPolicy implements AggregateDataSource {

	@Override
	public String getId() {
		return "MeanReceiverInvPolicy";
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
		return sumStrategies(receivers).assign(Functions.div(receivers.size()));
	}

	@Override
	public void reset() {		
	}
	
	private DoubleMatrix1D sumStrategies(IndexedIterable<Receiver> receivers) {
		DoubleMatrix1D result = receivers.iterator().next().investmentPolicy().copy();
		result.assign(0);
		for (Receiver receiver : receivers) {
			result = result.assign(receiver.investmentPolicy(), plus);
		}
		return result;
	}
	
	DoubleDoubleFunction plus = new DoubleDoubleFunction() {
	    public double apply(double a, double b) { return a+b; }
	}; 

}
