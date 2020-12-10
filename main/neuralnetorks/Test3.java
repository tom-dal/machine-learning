package neuralnetorks;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.core.LearningCore;
import neuralnetorks.enums.Models;
import neuralnetorks.model.Network;

public class Test3 {
	
	
	

	public static void main(String[] args) {
				
		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);
		
		builder.addLayer(5).addLayer(5).addLayer(1).setInputSize(1).setNetworkName("Asghenauei");
		
		Network network = builder.getNetwork();
		
		LearningCore lc = new LearningCore(network);
		
		lc.getPropagationFactor(network.getLayers().getFirst().getNeurons().stream().findAny().get());
		
		
		
		

	}

		

}