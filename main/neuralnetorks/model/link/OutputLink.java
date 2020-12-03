package neuralnetorks.model.link;

public class OutputLink extends AbstractLink {

	/* OutputLink inteso come parametro di output della rete. Il suo peso è sempre 1 e il suo ruolo è memorizzare il valore
	 * ritornato dal neurone dell'output layer a cui è associato;
	 */
	
	public OutputLink() {
		super();
		this.weight = 1;
	}
	
	
	@Override
	public void setWeight(double weight) {
		System.out.println("!!!!!!!!! setWeight(double weight) invocato da un'istanza di OutputLink !!!!!!!!!!");
	}
	
}
