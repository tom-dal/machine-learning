package neuralnetorks.model;

import neuralnetorks.model.neuron.AbstractNeuron;

public class Link {
	
	private AbstractNeuron fromNeuron;
	private AbstractNeuron toNeuron;
	private double weight;
	private double value;


	public AbstractNeuron getFromNeuron() {
		return fromNeuron;
	}


	public void setFromNeuron(AbstractNeuron fromNeuron) {
		this.fromNeuron = fromNeuron;
	}


	public AbstractNeuron getToNeuron() {
		return toNeuron;
	}


	public void setToNeuron(AbstractNeuron toNeuron) {
		this.toNeuron = toNeuron;
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		this.weight = weight;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}
	
	

	

}
