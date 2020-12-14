package neuralnetorks.model.layer;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.model.neuron.OutputNeuron;

public class OutputLayer extends AbstractLayer {

	
	public OutputLayer(int numberOfNeurons, ActivationFunctions activationFunction) {
		super();
		this.id="Output";
		for (int i = 0; i < numberOfNeurons; i++) {
			neurons.add(new OutputNeuron(activationFunction));
		}
		neurons.forEach(n -> {
			n.setLayer(this);
			n.setId(neuronCount + "-OUT");
		});	
	}


	
}
