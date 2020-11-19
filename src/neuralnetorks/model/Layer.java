package neuralnetorks.model;

import java.util.Set;

public abstract class Layer {
	
	protected Set<Neuron> neurons;

	public Set<Neuron> getNeurons() {
		return neurons;
	}

	public void setNeurons(Set<Neuron> neurons) {
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
	
	

}
