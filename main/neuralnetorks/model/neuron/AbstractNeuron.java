package neuralnetorks.model.neuron;

import java.util.HashSet;
import java.util.Set;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.function.interfaces.ActivationFunctionInterface;
import neuralnetorks.model.layer.AbstractLayer;
import neuralnetorks.model.link.AbstractLink;

public abstract class AbstractNeuron {

	private  String id;

	protected Set<AbstractLink> inLinks;
	protected Set<AbstractLink> outLinks;
	
	protected double bias;
	protected double nextBias;
	
	protected ActivationFunctionInterface activationFunction;

	protected AbstractLayer layer;
	
	protected AbstractNeuron(ActivationFunctions activationFunction) {
		this.inLinks = new HashSet<>();
		this.outLinks = new HashSet<>();
		this.activationFunction = ActivationFunctionInterface.getInstance(activationFunction);
	}

	public AbstractLayer getLayer() {
		return layer;
	}

	public void setLayer(AbstractLayer layer) {
		this.layer = layer;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
	}


	public  double getBias() {
		return bias;
	}

	public abstract void setBias(double bias);
	
	public abstract void setNextBias(double bias);
	
	public void updateBias() {
		this.bias = nextBias;
	}

	public ActivationFunctionInterface getActivationFunction() {
		return activationFunction;
	}

	public void process(int index) {
		double output = activationFunction.apply(bias + inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum());
		outLinks.forEach(link -> {
			link.setValue(output);
			link.addToValueBatch(output,index);
		});
	}
	
	public void process() {
		double output = activationFunction.apply(bias + inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum());
		outLinks.forEach(link -> link.setValue(output));
	}

	public abstract Set<AbstractLink> getInLinks();

	public abstract Set<AbstractLink> getOutLinks();
}
