package neuralnetorks.model.neuron;

import java.util.Set;

import neuralnetorks.model.link.AbstractLink;
import neuralnetorks.utils.MathUtilities;

public class Neuron extends AbstractNeuron{

	private double nextBias;
	
	public Neuron() {
		super();
		this.bias = MathUtilities.getRandomDouble();
	}
		
	public void setNextBias(double nextBias) {
		this.nextBias = nextBias;
	}
	
	public void updateBias() {
		this.bias = nextBias;
	}

	@Override
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	@Override
	public Set<AbstractLink> getInLinks() {
		return inLinks;
	}

	@Override
	public Set<AbstractLink> getOutLinks() {
		return outLinks;
	}	

}
