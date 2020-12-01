package neuralnetorks;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtilities;

public class Test2 {

	public static void main(String[] args) {
		
		
	
		
		double[] inputData = new double[200];
		
		double[] targetData = new double[200];
		
		
		for (int i = 0; i < inputData.length; i++) {
			inputData[i] = i;
			targetData[i] = 33 + 23.1*i;
		}
		
		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);
		
		builder.addLayer(5).addLayer(5).addLayer(5).addLayer(1).setInputSize(1).setNetworkName("Asghenauei");
		
		Network network = builder.getNetwork();
		
		LearningCore lc = new LearningCore(network);
		
		
		double[][] inputDataArray = new double[inputData.length][];
		for (int i = 0; i < inputDataArray.length; i++) {
			inputDataArray[i] = MathUtilities.doubleToArray(inputData[i]);
		}
		lc.setLearningRate(0.001);
		lc.configuration(NetworkOptions.NUMERICAL_DIFFERENTIATION,true);
		lc.configuration(NetworkOptions.INPUT_BATCH_NORMALIZATION, true);
		lc.configuration(NetworkOptions.TARGET_BATCH_NORMALIZATION, true);
		
		lc.learn(inputDataArray, targetData, 30);
		
		

		
		
		
		double[] test = new double [1];
		 
		for (int i = 0; i <10; i++) {
			test[0]= i*10;
			double prediction = lc.predict(test);
			System.out.println("Input: " + test[0] + "  Predicted: " +prediction+ "    Actual: "+(33+23.1*test[0]) 
					+ "   Error: " + ((33+23.1*test[0]) - prediction) ) ;
			
		}
		
		
		
		
		
		
		

	}

		

}