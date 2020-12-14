package neuralnetorks.function.interfaces;

public interface ErrorFunctionInterface {
	
	public double getError(double[][] outputBatch, double[][] targetData);


}
