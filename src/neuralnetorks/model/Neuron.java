package neuralnetorks.model;

import java.util.Map;

import neuralnetorks.function.ActivationFunction;

public class Neuron {
	
	private Map<Integer, InputCouple> inputs;
	private double bias;
	private ActivationFunction activationFunction;
	private double output;
	
	public Neuron(ActivationFunction activationFunction) {
		this.activationFunction=activationFunction;
	}
	
	public Map<Integer, InputCouple> getInputs() {
		return inputs;
	}
	public void setInputs(Map<Integer, InputCouple> inputs) {
		this.inputs = inputs;
	}
	public double getBias() {
		return bias;
	}
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public void addToOutput(double weightedInput) {
		this.output+=weightedInput;
	}
	
	public double processOutput() {
		output = 0;
		this.inputs.forEach((index,input) -> output+=input.multiplyInputWeight());
		return activationFunction==null? output : activationFunction.processOutput(output);
	}
	

}
