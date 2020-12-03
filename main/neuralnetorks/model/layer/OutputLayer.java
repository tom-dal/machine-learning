package neuralnetorks.model.layer;



import neuralnetorks.model.neuron.OutputNeuron;

public class OutputLayer extends AbstractLayer {

	
	public OutputLayer(int numberOfNeurons) {
		super();
		this.id="Output";
		for (int i = 0; i < numberOfNeurons; i++) {
			neurons.add(new OutputNeuron());
		}
		neurons.forEach(n -> {
			n.setLayer(this);
			n.setId(neuronCount + "-OUT");
		});	
	}



	
}
