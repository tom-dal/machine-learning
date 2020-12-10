package neuralnetorks.model;

import java.util.LinkedList;

import neuralnetorks.enums.Models;
import neuralnetorks.model.layer.AbstractLayer;
import neuralnetorks.model.layer.InputLayer;
import neuralnetorks.model.layer.OutputLayer;

public class Network {
	
	private Models model;
	
	private LinkedList<AbstractLayer> layers = new LinkedList<>();
	
	public LinkedList<AbstractLayer> getLayers() {
		return layers;
	}
	public Models getModel() {
		return model;
	}
	public void setModel(Models model) {
		this.model = model;
	}
	
	public InputLayer getInputLayer() {
		return (InputLayer) layers.getFirst();
	}
	
	public OutputLayer getOutputLayer() {
		return (OutputLayer) layers.getLast();
	}
	

}
