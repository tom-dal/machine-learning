package neuralnetorks.model;


import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.utils.MathUtils;

public class Neuron {
	
	private InputCouple[] inputs;
	private double bias;
	private ActivationFunction activationFunction;
	private double output;
	
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
	
	public double processOutput() {
		output = 0;
		for (InputCouple inputCouple : inputs) {
			output+= (inputCouple.getInput()*inputCouple.getWeight());
		}
		return activationFunction==null? output : activationFunction.processOutput(output);
	}
	
	public InputCouple[] getInputs() {
		return inputs;
	}

	public void setInputs(InputCouple[] inputs) {
		this.inputs = inputs;
	}

	public void initializeInputCouples(int inputSize) {
		InputCouple[] inputCouples = new InputCouple[inputSize];
		for (int i = 0; i < inputCouples.length; i++) {
			inputCouples[i] = new InputCouple();
			inputCouples[i].setWeight(MathUtils.random.nextDouble());
		}
		
	}
	

}
