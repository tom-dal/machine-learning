package neuralnetorks.model.link;

public class InputLink extends AbstractLink {

	/* InputLink inteso come input della rete, non input del singolo neurone. Viene quindi
	 * associato come inLink ad un InputLayer ed il suo peso è sempre 1;
	 */
	
	public InputLink() {
		super();
		this.weight = 1;
	}
	
	
	@Override
	public void setWeight(double weight) {
		System.out.println("!!!!!!!!! setWeight(double weight) invocato da un'istanza di InputLink !!!!!!!!!!");
	}
}
