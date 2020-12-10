package neuralnetorks.function;

import neuralnetorks.utils.MathUtilities;

public class MeanSquaredError implements ErrorFunctionInterface {

	@Override
	public double getError(double[][] outputBatch, double[][] targetData) {
		return MathUtilities.meanSquaredError(outputBatch, targetData);
	}


}
