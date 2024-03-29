package neuralnetorks.model.neuron;

import java.util.Set;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.model.link.AbstractLink;
import neuralnetorks.model.link.InputLink;

public class InputNeuron  extends AbstractNeuron {
	
	/* Pu� avere un solo link in ingresso e bias sempre zero (override del setter che viene reso inefficace).
	 *Il link in ingresso viene aggiunto nel costruttore e il set non pu� essere pi� modificato.*/
	
	public InputNeuron(){
		super(ActivationFunctions.IDENTITY);
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
		return null; /*Deve ritornare null in modo che il neurone non venga considerato
		quando questo metodo � invocato da UpdateLayerInLinks*/
	}


	@Override
	public void setNextBias(double nextBias) {
		this.nextBias = 0; /*Fail-safe*/
	}
}
