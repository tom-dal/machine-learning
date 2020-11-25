package neuralnetorks.model.layer;

import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.utils.IdUtil;

public class Layer extends AbstractLayer {
	
	private Neuron[] neurons;
	private Layer previous;
	private Layer next;

	
	
	public Layer(int neuronsNumber) {
		this.id = IdUtil.getNewId();
		this.neurons = new Neuron[neuronsNumber];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron();
		}
	}


	public Layer getPrevious() {
		return previous;
	}


	public void setPrevious(Layer previous) {
		this.previous = previous;
	}


	public Layer getNext() {
		return next;
	}


	public void setNext(Layer next) {
		this.next = next;
	}


	

	
	public Neuron[] getNeurons() {
		return neurons;
	}

}
