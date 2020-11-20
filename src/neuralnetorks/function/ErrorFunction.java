package neuralnetorks.function;

public interface ErrorFunction {
	
	public double getError(double[][] predicted, double[] actual);


}
