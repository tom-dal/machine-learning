package neuralnetorks.function.error;

import neuralnetorks.function.interfaces.ErrorFunctionInterface;
import neuralnetorks.utils.MathUtilities;

public class MeanSquaredError implements ErrorFunctionInterface {

	@Override
	public double getError(double[][] outputBatch, double[][] targetData) {
		return MathUtilities.meanSquaredError(outputBatch, targetData);
	}


}
