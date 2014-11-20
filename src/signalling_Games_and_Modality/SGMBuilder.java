package signalling_Games_and_Modality;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;

public class SGMBuilder implements ContextBuilder<Object> {
	


	@Override
	public Context build(Context<Object> context) {
		
		context.setId("Signalling Games and Modality");
		
		ContinuousSpaceFactory spaceFactory = 
				ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = 
				spaceFactory.createContinuousSpace("space", context, 
						new RandomCartesianAdder<Object>(), 
						new repast.simphony.space.continuous.WrapAroundBorders(), 50, 50);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, 
				new GridBuilderParameters<Object>(new WrapAroundBorders(), 
						new SimpleGridAdder<Object>(), true, 50, 50));
		
		NetworkBuilder networkBuilder = new NetworkBuilder("network", context, true);
		Network<Object> network = networkBuilder.buildNetwork();
		
		int monsterCount = 500;
		for (int i = 0; i < monsterCount; i++) {
			context.add(new Monster(space, grid, network, 0, 0));
		}
		
		int senderCount = 100;
		for (int i = 0; i < senderCount; i++) {
			context.add(new Sender(space, grid, network, 25, 
					Utils.initialSenderStrat));
		}
		
		int receiverCount = 100;
		for (int i = 0; i < receiverCount; i++) {
			context.add(new Receiver(space, grid, network, 25, 
					Utils.initialReceiverStrat,
					Utils.initialReceiverInvestmentPolicy));
		}
		
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int)pt.getX(), (int)pt.getY());
		}
		
		return context;
	}

}
