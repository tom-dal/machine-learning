package neuralnetorks.model;

import java.util.Map;
import java.util.Set;

public abstract class Layer {
	
	protected Map<Long, Neuron> neurons;
	
	protected Layer previous;
	protected Layer next;
	private boolean outputLayer = Boolean.FALSE;

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

	public Map<Long,Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(Map<Long, Neuron> neurons) {
		this.neurons = neurons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((neurons == null) ? 0 : neurons.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Layer other = (Layer) obj;
		if (neurons == null) {
			if (other.neurons != null)
				return false;
		} else if (!neurons.equals(other.neurons))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Number of neurons: ");
		builder.append(neurons.size());
		return builder.toString();
	}

	public abstract double[] process(double[] input);

	public boolean isOutputLayer() {
		return outputLayer;
	}

	public void setOutputLayer(boolean outputLayer) {
		this.outputLayer = outputLayer;
	}
	
	

}
