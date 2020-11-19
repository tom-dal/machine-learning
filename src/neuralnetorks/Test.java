package neuralnetorks;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.model.Network;

public class Test {

	public static void main(String[] args) {
		
		double[] inputData = {2,3,4,5,6,7,9};
		
		NetworkBuilder builder = new NetworkBuilder();
		
		builder.addDenseLayer(10)
		.addDenseLayer(10).addDenseLayer(1);
		
		Network fcn = builder.getNetwork();
		
		fcn.initialize(inputData);
	
		System.out.println(fcn.toString());
		
		
	}

		

}
