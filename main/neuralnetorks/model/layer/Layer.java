package neuralnetorks.model.layer;

import java.util.Arrays;

import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.utils.IdUtil;

public class Layer extends AbstractLayer {
	
	private Long id;
	
	private Neuron[] neurons;
	private Layer previous;
	private Layer next;

	
	
	public Layer(int neuronsNumber) {
		this.neurons = new Neuron[neuronsNumber];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron();
		}
		Arrays.stream(neurons).forEach(n -> n.setLayer(this));
		this.id = IdUtil.getNextLayerNumber();
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
	
	public void process(int index) {
		Arrays.stream(this.neurons).forEach(neuron -> neuron.process(index));
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void process() {
		Arrays.stream(this.neurons).forEach(neuron -> neuron.process());
		
	}

}
