package neuralnetorks;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtilities;

public class Test2 {

	public static void main(String[] args) {

		double[] inputData = new double[800];

		double[] targetData = new double[800];

		for (int i = 0; i < inputData.length; i++) {
			inputData[i] = i;
			targetData[i] = 33 + 23.1 * i;
		}

		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);

		builder.addDeepLayer(2).addDeepLayer(2).addDeepLayer(1).setInputSize(1).setNetworkName("Asghenauei");

		Network network = builder.getNetwork();

		LearningCore lc = new LearningCore(network);

		double[][] inputDataArray = new double[inputData.length][];
		for (int i = 0; i < inputDataArray.length; i++) {
			inputDataArray[i] = MathUtilities.doubleToArray(inputData[i]);
		}
		lc.setLearningRate(0.0000001);
		lc.configuration(NetworkOptions.NUMERICAL_DIFFERENTIATION, true);
		lc.configuration(NetworkOptions.INPUT_BATCH_CENTERING, true);
//		lc.configuration(NetworkOptions.TARGET_BATCH_NORMALIZATION, true);
		lc.configuration(ErrorFunctions.MEAN_SQUARED_ERROR);

		lc.learn(inputDataArray, targetData, 22);
	
		lc.setLearningRate(0.00000000001);

		lc.learn(500);
		

		
		


		
		double[] test = new double [1];

		for (int i = 0; i < 10; i++) {
			test[0] = i * 10;
			double prediction = lc.predict(test);
			double actual = 33 + 23.1 * test[0];
			System.out.println("Input: " + test[0] + "  Predicted: " + prediction + "    Actual: "
					+ actual + "   Error%: " + Math.abs((actual - prediction)/actual)*100);

		}
		
		
		
		
		}
}