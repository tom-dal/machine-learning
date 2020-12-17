package neuralnetorks.function.interfaces;

import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.function.error.AbsoluteError;
import neuralnetorks.function.error.MeanAbsoluteError;
import neuralnetorks.function.error.MeanSquaredError;

public interface ErrorFunctionInterface {
	
	public double getError(double[][] outputBatch, double[][] targetData);
	
	public static ErrorFunctionInterface getInstance(ErrorFunctions errorFunction) {
		if(errorFunction.equals(ErrorFunctions.ABSOLUTE_ERROR)) return AbsoluteError.getIstance();
		if(errorFunction.equals(ErrorFunctions.MEAN_ABSOLUTE_ERROR)) return MeanAbsoluteError.getIstance();
		if(errorFunction.equals(ErrorFunctions.MEAN_SQUARED_ERROR)) return MeanSquaredError.getIstance();
		else return null;
	}


}
