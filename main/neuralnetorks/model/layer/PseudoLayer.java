package neuralnetorks.model.layer;


import neuralnetorks.model.neuron.PseudoNeuron;
import neuralnetorks.utils.IdUtil;

public class PseudoLayer extends AbstractLayer {

	private PseudoNeuron[] pseudoNeurons;	
	
	public PseudoLayer(int paramsNumber) {
		this.id = IdUtil.getNewId();
		this.pseudoNeurons = new PseudoNeuron[paramsNumber];
		for (int i = 0; i < pseudoNeurons.length; i++) {
			pseudoNeurons[i] = new PseudoNeuron();
		}
	}

	public PseudoNeuron[] getPseudoNeurons() {
		return pseudoNeurons;
	}

	
}
