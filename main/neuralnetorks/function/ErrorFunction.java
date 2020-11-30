package neuralnetorks.function;

public interface ErrorFunction {
	
	public double getError(double[] outputBatch, double[] actual);


}
