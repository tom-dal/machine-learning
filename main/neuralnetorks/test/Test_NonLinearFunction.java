package neuralnetorks.test;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.core.LearningCore;
import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtilities;

public class Test_NonLinearFunction{

	public static void main(String[] args) {

		double[] inputData = new double[200];

		double[] targetData = new double[200];

		for (int i = 0; i < inputData.length; i++) {
			inputData[i] = i;
			targetData[i] = testFunction(i);
		}

		NetworkBuilder builder = new NetworkBuilder(Models.LINEAR_REGRESSION);

		builder
		.addLayer(1)
		.addLayer(5, ActivationFunctions.LReLU)
		.addLayer(5, ActivationFunctions.LReLU)
		.addLayer(1)
		.setInputSize(1)
		.setNetworkName("Test appendimento funzione non lineare");

		Network network = builder.getNetwork();

		LearningCore lc = new LearningCore(network);

		double[][] inputDataArray = new double[inputData.length][];
		for (int i = 0; i < inputDataArray.length; i++) {
			inputDataArray[i] = MathUtilities.doubleToArray(inputData[i]);
		}
		
		double[][] targetDataArray = new double[targetData.length][];
		for (int i = 0; i < targetDataArray.length; i++) {
			targetDataArray[i] = MathUtilities.doubleToArray(targetData[i]);
		}
		
		
		lc.setLearningRate(10e-8);
		lc.configuration(NetworkOptions.NUMERICAL_DIFFERENTIATION, true);
		lc.configuration(NetworkOptions.INPUT_BATCH_CENTERING, true);
		lc.configuration(NetworkOptions.INPUT_BATCH_NORMALIZATION, true);
		lc.configuration(NetworkOptions.TARGET_BATCH_CENTERING, true);
		lc.configuration(NetworkOptions.TARGET_BATCH_NORMALIZATION, true);
		lc.configuration(ErrorFunctions.MEAN_SQUARED_ERROR);
  
		lc.learn(inputDataArray, targetDataArray, 200);
	
		
		double[] test = new double [1];

		for (int i = 0; i < 10; i++) {
			test[0] = i * 10;
			double[] prediction = lc.predict(test);
			double actual = testFunction(test[0]);
			System.out.println("Input: " + test[0] + "  Predicted: " + prediction[0] + "    Actual: "
					+ actual + "   Error%: " + Math.abs((actual - prediction[0])/actual)*100);

		}
		
		
		
		
		}
	
	private static double testFunction(double input) {
		return 50 + input*input;
	}
}