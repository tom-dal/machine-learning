package neuralnetorks.model.link;

import neuralnetorks.model.neuron.AbstractNeuron;

public abstract class AbstractLink {
	
	protected AbstractNeuron fromNeuron;
	protected AbstractNeuron toNeuron;
	protected double weight;
	protected double value;
	protected double nextWeight;
	
	protected double[] valueBatch; /* Un record dei valori assunti per ogni batch in input durante il training*/
	

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


	public abstract void setWeight(double weight);


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
