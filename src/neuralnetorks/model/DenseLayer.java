package neuralnetorks.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;

public class DenseLayer extends Layer{
	
	
	public DenseLayer(int neuronsNumber) {
		this.neurons = Stream.of(new Neuron(null)).limit(neuronsNumber).collect(Collectors.toSet());
	}
	
	public DenseLayer(int neuronsNumber, ActivationFunction activationFunction) {
		this.neurons = Stream.of(new Neuron(activationFunction)).limit(neuronsNumber).collect(Collectors.toSet());
	}

}
