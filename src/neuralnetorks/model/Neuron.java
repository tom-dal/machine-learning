package neuralnetorks.model;



import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.utils.MathUtils;

public class Neuron {
	
	private double[] weights;
	private double bias;
	
	public Neuron() {
		
	}
	
	public double getBias() {
		return bias;
	}
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public double processOutput(double[] inputs) {
		double output = 0;
		for (int i = 0; i < inputs.length; i++) {
			output += (inputs[i]*weights[i] + bias);
		}
		return output;
	}
	

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public void setRandomWeights(int inputSize) {
		this.weights = new double[inputSize];
		this.weights = Arrays.stream(new double[this.weights.length]).map(d -> d=MathUtils.random.nextDouble()).toArray();
	}

	public void setRandomBias() {
		this.bias = MathUtils.getRandomDouble();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bias);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(weights);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Neuron other = (Neuron) obj;
		if (Double.doubleToLongBits(bias) != Double.doubleToLongBits(other.bias))
			return false;
		if (!Arrays.equals(weights, other.weights))
			return false;
		return true;
	}
	
	
	

}
