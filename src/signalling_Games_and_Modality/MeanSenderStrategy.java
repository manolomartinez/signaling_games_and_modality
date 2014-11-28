package signalling_Games_and_Modality;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.context.Context;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;

public class MeanSenderStrategy implements AggregateDataSource {

	@Override
	public String getId() {
		return "MeanSenderStrategy";
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
		IndexedIterable<Sender> senders = context.getObjects(Sender.class);
		return sumStrategies(senders).assign(Functions.div(senders.size()));
	}

	@Override
	public void reset() {		
	}
	
	private DoubleMatrix2D sumStrategies(IndexedIterable<Sender> senders) {
		DoubleMatrix2D result = senders.iterator().next().strategy().copy();
		result.assign(0);
		for (Sender sender : senders) {
			result = result.assign(sender.strategy(), plus);
		}
		return result;
	}
	
	DoubleDoubleFunction plus = new DoubleDoubleFunction() {
	    public double apply(double a, double b) { return a+b; }
	}; 

}
