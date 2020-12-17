package neuralnetorks.function.error;

import neuralnetorks.function.interfaces.ErrorFunctionInterface;
import neuralnetorks.utils.MathUtilities;

public class AbsoluteError implements ErrorFunctionInterface {
	
	private static AbsoluteError istance;

	@Override
	public double getError(double[][] outputBatch, double[][] targetData) {
		return MathUtilities.absoluteError(outputBatch, targetData);
	}
	
	private AbsoluteError() {}

	public static ErrorFunctionInterface getIstance() {
		
		if (istance == null) istance = new AbsoluteError();
		return istance;
	}



}
