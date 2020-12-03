package neuralnetorks.model.neuron;

import java.util.Set;

import neuralnetorks.model.link.AbstractLink;
import neuralnetorks.model.link.InputLink;

public class InputNeuron  extends AbstractNeuron {
	
	/* Può avere un solo link in ingresso e bias sempre zero (override del setter che viene reso inefficace).
	 *Il link in ingresso viene aggiunto nel costruttore e il set non può essere più modificato.*/
	
	public InputNeuron(){
		super();
		this.bias = 0;
		this.inLinks.add(new InputLink());
	}
	
	
	@Override
	public void setBias(double bias) {
		System.out.println("!!!!!!!!! setBias(double bias) invocato da un'istanza di InputNeuron !!!!!!!!!!");
	}
	
	public AbstractLink getInLink() {
		return inLinks.stream().findAny().get();
	}

	public Set<AbstractLink> getOutLinks() {
		return outLinks;
	}


	@Override
	public Set<AbstractLink> getInLinks() {
		System.out.println("!!!!!!!!! getInlinks() invocato da un'istanza di InputNeuron !!!!!!!!!!");
		return null;
	}
}
