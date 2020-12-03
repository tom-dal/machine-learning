package neuralnetorks.model.layer;



import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.utils.IdUtil;

public class DeepLayer extends AbstractLayer {
	
	public DeepLayer(int numberOfNeurons) {
		super();
		this.id = IdUtil.getNextDeepLayerNumber().toString();
		for (int i = 0; i < numberOfNeurons; i++) {
			neurons.add(new Neuron());
			neuronCount++;
		}
		neurons.forEach(n -> {
			n.setLayer(this);
			n.setId(neuronCount + "-L" + this.id);
		});
	}

}
