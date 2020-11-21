package neuralnetorks.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.utils.HashUtil;

public class DenseLayer extends Layer{
	
	
	public DenseLayer(int neuronsNumber) {
//		this.neurons = Stream.generate(()->new Neuron()).limit(neuronsNumber).collect(Collectors.toSet());
		this.neurons = new HashMap<>();
		for (int i = 0; i < neuronsNumber; i++) {
			Neuron neuron = new Neuron();
			this.neurons.put(HashUtil.getNewHashCode(), neuron);
		}
	}
	

	@Override
	public double[] process(double[] input) {
		return this.getNeurons().stream().mapToDouble(neuron -> neuron.processOutput(input)).toArray();
	}

}
