package neuralnetorks.model.neuron;

public class Neuron extends AbstractNeuron{

	private double nextBias;
	
		
	public void setNextBias(double nextBias) {
		this.nextBias = nextBias;
	}
	
	public void updateBias() {
		this.bias = nextBias;
	}

	public void process() {
		double output = bias +  inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum();
		outLinks.forEach(link -> link.setValue(output));
	}


	

}
