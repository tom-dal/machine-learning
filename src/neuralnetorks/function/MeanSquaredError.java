package neuralnetorks.function;

import neuralnetorks.utils.MathUtils;

public class MeanSquaredError implements ErrorFunction {

	@Override
	public double getError(double[][] predicted, double[] actual) {
		return MathUtils.meanSquaredError(predicted, actual);
	}

}
