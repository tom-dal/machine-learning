package neuralnetorks.model.layer;



import java.util.Set;

import neuralnetorks.model.neuron.InputNeuron;


public class InputLayer extends AbstractLayer {

	
	public InputLayer(int numberOfNeurons) {
		super();
		this.id = "Input";
		for (int i = 0; i < numberOfNeurons; i++) {
			neurons.add(new InputNeuron());
		}
		neurons.forEach(n -> {
			n.setLayer(this);
			n.setId(neuronCount + "-IN");
		});	
	}



	
}
