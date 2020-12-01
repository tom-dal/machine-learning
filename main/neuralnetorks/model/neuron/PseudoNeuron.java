package neuralnetorks.model.neuron;

import java.util.ArrayList;

public class PseudoNeuron  extends AbstractNeuron {
	
	
	@Override
	public void process(int index) {
		double output = inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum();
		outLinks.forEach(link -> {
			link.setValue(output);
			link.addToValueBatch(output,index);
		});
	}
	
	public void emptyInputLinks() {
		this.inLinks = new ArrayList<>();
	}

	public void process() {
		double output = inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum();
		outLinks.forEach(link -> link.setValue(output));
	}
}
