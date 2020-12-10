package neuralnetorks.function;

public interface ErrorFunctionInterface {
	
	public double getError(double[][] outputBatch, double[][] targetData);


}
