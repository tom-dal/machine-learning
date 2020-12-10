package neuralnetorks.model.neuron;


import java.util.Set;

import neuralnetorks.model.link.AbstractLink;
import neuralnetorks.model.link.OutputLink;
import neuralnetorks.utils.MathUtilities;

public class OutputNeuron  extends AbstractNeuron {
	
	/* Può avere un solo link in uscita: viene aggiunto nel costruttore e il set non può essere più modificato.*/
	
	public OutputNeuron() {
		super();
		this.bias = MathUtilities.getRandomDouble();
		this.outLinks.add(new OutputLink());
	}
	
	@Override
	public void setBias(double bias) {
		this.bias = bias;
	}
	
	public Set<AbstractLink> getInLinks() {
		return inLinks;
	}

	public AbstractLink getOutLink() {
		return outLinks.stream().findAny().get();
	}

	@Override
	public Set<AbstractLink> getOutLinks() {
		System.out.println("!!!!!!!!! getOutLinks() invocato da un'istanza di OutputNeuron !!!!!!!!!!");
		return null;
	}

	@Override
	public void setNextBias(double nextBias) {
		this.nextBias=bias;
	}
}
