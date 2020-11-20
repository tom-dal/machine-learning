package neuralnetorks;

import java.util.Arrays;

import neuralnetorks.function.MeanSquaredError;
import neuralnetorks.model.Layer;
import neuralnetorks.model.Network;
import neuralnetorks.utils.MathUtils;

public class LearningCore {

	private double learningRate;
	private MeanSquaredError errorFunction;
	
	public enum ErrorFunctions{
		MEAN_SQUARED_ERROR;
	}

	public void setLearningRate(double lr) {
		this.learningRate=lr;
	}

	public void setErrorFunction(ErrorFunctions function) {
		if (function.equals(ErrorFunctions.MEAN_SQUARED_ERROR)) {
			this.errorFunction=new MeanSquaredError();
		}	
	}

	public void teach(Network fcn, double[][] inputData, double[] targetData, int iterations) {
	
		for (int i = 0; i < iterations; i++) {
			double[][] predicted = new double[inputData.length][inputData[0].length];
			for (int j = 0; j < inputData.length; j++) {
				double[] input = inputData[j];
				double[] output = null;
				for (Layer layer : fcn.getLayers()) {
					output = layer.process(input);
				}
				predicted[j] = output;
			}
			double mse = this.errorFunction.getError(predicted , targetData);
		}
		
	}
	
	

}
