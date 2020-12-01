package neuralnetorks.function;

import neuralnetorks.utils.MathUtilities;

public class MeanSquaredError implements ErrorFunctionInterface {

	@Override
	public double getError(double[] predicted, double[] actual) {
		return MathUtilities.meanSquaredError(predicted, actual);
	}


}
