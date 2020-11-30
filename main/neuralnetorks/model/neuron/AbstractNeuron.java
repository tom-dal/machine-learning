package neuralnetorks.model.neuron;

import java.util.ArrayList;
import java.util.List;

import neuralnetorks.model.Link;
import neuralnetorks.model.layer.Layer;
import neuralnetorks.utils.IdUtil;
import neuralnetorks.utils.MathUtilities;

public abstract class AbstractNeuron {

	private final Long id;

	protected List<Link> inLinks = new ArrayList<>();
	protected List<Link> outLinks = new ArrayList<>();
	
	protected Layer layer;

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	protected double bias;

	public AbstractNeuron() {
		this.id = IdUtil.getNewId();
		this.bias = MathUtilities.getRandomDouble();
	}

	public List<Link> getInLinks() {
		return inLinks;
	}

	public List<Link> getOutLinks() {
		return outLinks;
	}

	public Long getId() {
		return id;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public void process() {
		double output = bias + inLinks.stream().mapToDouble(link -> link.getValue() * link.getWeight()).sum();
		outLinks.forEach(link -> link.setValue(output));
	}
}
