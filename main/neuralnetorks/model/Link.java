package neuralnetorks.model;

import neuralnetorks.model.neuron.AbstractNeuron;

public class Link {
	
	private AbstractNeuron fromNeuron;
	private AbstractNeuron toNeuron;
	private double weight;
	private double value;
	private double nextWeight;
	
	private double[] valueBatch;
	

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


	public double getNextWeight() {
		return nextWeight;
	}


	public void setNextWeight(double nextWeight) {
		this.nextWeight = nextWeight;
	}
	
	public void updateWeight() {
		this.weight = nextWeight;
		
	}


	public double[] getValueBatch() {
		return valueBatch;
	}


	public void setValueBatch(double[] valueBatch) {
		this.valueBatch = valueBatch;
	}


	public void addToValueBatch(double output, int index) {
		this.valueBatch[index] = output;
	}



	

}
