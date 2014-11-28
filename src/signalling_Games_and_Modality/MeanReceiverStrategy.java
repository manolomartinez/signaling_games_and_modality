package signalling_Games_and_Modality;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.context.Context;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;

public class MeanReceiverStrategy implements AggregateDataSource {

	@Override
	public String getId() {
		return "MeanReceiverStrategy";
	}

	@Override
	public Class<?> getDataType() {
		return DoubleMatrix2D.class;
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
	
	private DoubleMatrix2D sumStrategies(IndexedIterable<Receiver> receivers) {
		DoubleMatrix2D result = receivers.iterator().next().strategy().copy();
		result.assign(0);
		for (Receiver receiver : receivers) {
			result = result.assign(receiver.strategy(), plus);
		}
		return result;
	}
	
	DoubleDoubleFunction plus = new DoubleDoubleFunction() {
	    public double apply(double a, double b) { return a+b; }
	}; 

}
