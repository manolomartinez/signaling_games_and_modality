package signalling_Games_and_Modality;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.jet.math.Functions;
import repast.simphony.context.Context;
import repast.simphony.data2.AggregateDataSource;
import repast.simphony.util.collections.IndexedIterable;

public class MeanAirInvestment implements AggregateDataSource {

	@Override
	public String getId() {
		return "MeanAirInvestment";
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
		return meanAir(receivers);
	}

	@Override
	public void reset() {		
	}
	
	private double meanAir(IndexedIterable<Receiver> receivers) {
		SummaryStatistics data = new SummaryStatistics();
		for (Receiver receiver : receivers) {
			data.addValue(receiver.investmentPolicy().get(0));
		}
		return data.getMean();
	}
}
