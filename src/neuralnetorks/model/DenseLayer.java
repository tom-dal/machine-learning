package neuralnetorks.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;

public class DenseLayer extends Layer{
	
	
	public DenseLayer(int neuronsNumber) {
		this.neurons = Stream.generate(()->new Neuron()).limit(neuronsNumber).collect(Collectors.toSet());
	}
	
	public DenseLayer(int neuronsNumber, ActivationFunction activationFunction) {
		this.neurons = Stream.generate(() -> new Neuron(activationFunction)).limit(neuronsNumber).collect(Collectors.toSet());
	}

	@Override
	public double[] process(double[] input) {
		return this.getNeurons().stream().mapToDouble(neuron -> neuron.processOutput(input)).toArray();
	}

}
