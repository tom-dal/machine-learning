package neuralnetorks.model.layer;



import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.utils.IdUtil;

public class DeepLayer extends AbstractLayer {
	
	public DeepLayer(int numberOfNeurons, ActivationFunctions activationFunction) {
		super();
		this.id = IdUtil.getNextDeepLayerNumber().toString();
		for (int i = 0; i < numberOfNeurons; i++) {
			neurons.add(new Neuron(activationFunction));
		}
		neurons.forEach(n -> {
			neuronCount++;
			n.setLayer(this);
			n.setId(neuronCount + "-L" + this.id);
		});
	}

}
