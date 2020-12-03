package neuralnetorks.model.layer;

import java.util.HashSet;
import java.util.Set;

import neuralnetorks.model.neuron.AbstractNeuron;

public abstract class AbstractLayer {

	
	protected String id;

	protected Set<AbstractNeuron> neurons;
	protected int neuronCount;

	private AbstractLayer previous;
	private AbstractLayer next;

	public AbstractLayer() {
		this.neurons = new HashSet<>();
		this.neuronCount=0;
	}
	
	public String getId() {
		return id;
	}

	public AbstractLayer getPrevious() {
		return previous;
	}

	public void setPrevious(AbstractLayer previous) {
		this.previous = previous;
	}

	public AbstractLayer getNext() {
		return next;
	}

	public void setNext(AbstractLayer next) {
		this.next = next;
	}

	public void process(int batchIndex) {
		neurons.forEach(neuron -> neuron.process(batchIndex));
	}

	public Set<AbstractNeuron> getNeurons() {
		return neurons;
	}

	public void process() {
		neurons.forEach(neuron -> neuron.process());
	}
}
