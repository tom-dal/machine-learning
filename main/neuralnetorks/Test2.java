package neuralnetorks;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.enums.Errors;
import neuralnetorks.enums.Models;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtilities;

public class Test2 {
	
	
	

	public static void main(String[] args) {
		
		
	
		
		double[] inputData = new double[50];
		
		double[] targetData = new double[50];
		
		
		for (int i = 0; i < inputData.length; i++) {
			inputData[i] = i;
			targetData[i] = 1 + 1.1*i;
		}
		
		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);
		
		builder.addLayer(3).addLayer(4).addLayer(1).setInputSize(1).setNetworkName("Asghenauei").initialize();
		
		Network network = builder.getNetwork();
		
		LearningCore lc = new LearningCore(0.000001, Errors.MEAN_SQUARED_ERROR);
		
		
		double[][] inputDataArray = new double[inputData.length][];
		for (int i = 0; i < inputDataArray.length; i++) {
			inputDataArray[i] = MathUtilities.doubleToArray(inputData[i]);
		}
		
		
		lc.teach(network, inputDataArray, targetData, 200);
		
		double[] test = {100};
		
		double prediction = lc.predict(network, test);
		
		System.out.println(prediction);
		
		
//		Network cloneNetwork = lc.cloneNetwork(fcn);
//		System.out.println(fcn.toString());
//		System.out.println(cloneNetwork.toString());
		
		

	}

		

}