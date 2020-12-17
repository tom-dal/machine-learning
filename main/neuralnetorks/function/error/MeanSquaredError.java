package neuralnetorks.function.error;

import neuralnetorks.function.interfaces.ErrorFunctionInterface;
import neuralnetorks.utils.MathUtilities;

public class MeanSquaredError implements ErrorFunctionInterface {
	
	public static MeanSquaredError istance;

	@Override
	public double getError(double[][] outputBatch, double[][] targetData) {
		return MathUtilities.meanSquaredError(outputBatch, targetData);
	}
	
	private MeanSquaredError() {}
	
	public static MeanSquaredError getIstance() {
		if (istance == null) istance = new MeanSquaredError();
		return istance;
	}


}
