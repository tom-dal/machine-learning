package neuralnetorks.model;



import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.utils.MathUtils;

public class Neuron {
	
	private double[] weights;
	private double bias;
	private ActivationFunction activationFunction;
	
	public Neuron() {
		
	}
	
	public Neuron(ActivationFunction activationFunction) {
		this.activationFunction=activationFunction;
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
			output += (inputs[i]*weights[i]);
		}
		return activationFunction==null? output : activationFunction.processOutput(output);
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
	

}
