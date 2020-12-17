package neuralnetorks.function.error;

import neuralnetorks.function.interfaces.ErrorFunctionInterface;
import neuralnetorks.utils.MathUtilities;

public class MeanAbsoluteError implements ErrorFunctionInterface{
	
	public static MeanAbsoluteError istance;

	@Override
	public double getError(double[][] outputBatch, double[][] targetData) {
		return MathUtilities.meanAbsoluteError(outputBatch, targetData);
	}
	
	private MeanAbsoluteError() {}
	
	public static MeanAbsoluteError getIstance() {
		if (istance == null) istance = new MeanAbsoluteError();
		return istance;
	}

}
