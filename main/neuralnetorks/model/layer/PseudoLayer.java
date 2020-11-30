package neuralnetorks.model.layer;


import java.util.Arrays;

import neuralnetorks.model.neuron.PseudoNeuron;

public class PseudoLayer extends AbstractLayer {

	private PseudoNeuron[] pseudoNeurons;	
	
	public PseudoLayer(int paramsNumber) {
		this.pseudoNeurons = new PseudoNeuron[paramsNumber];
		for (int i = 0; i < pseudoNeurons.length; i++) {
			pseudoNeurons[i] = new PseudoNeuron();
		}
	}

	public PseudoNeuron[] getPseudoNeurons() {
		return pseudoNeurons;
	}

	public void process() {
		Arrays.stream(this.pseudoNeurons).forEach(neuron -> neuron.process());
	}

	
}
