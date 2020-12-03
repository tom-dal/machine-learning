package neuralnetorks.function;

import neuralnetorks.utils.MathUtilities;

public class AbsoluteError implements ErrorFunctionInterface {

	@Override
	public double getError(double[] predicted, double[] actual) {
		return MathUtilities.absoluteError(predicted, actual);
	}


}
