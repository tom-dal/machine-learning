package neuralnetorks.model;

import java.util.LinkedList;

import neuralnetorks.enums.Models;
import neuralnetorks.model.layer.Layer;
import neuralnetorks.model.layer.PseudoLayer;

public class Network {
	
	private Models model;
	
	private PseudoLayer inputLayer;
	private LinkedList<Layer> layers = new LinkedList<>();
	
	public PseudoLayer getInputLayer() {
		return inputLayer;
	}
	public void setInputLayer(PseudoLayer inputLayer) {
		this.inputLayer = inputLayer;
	}
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	public Models getModel() {
		return model;
	}
	public void setModel(Models model) {
		this.model = model;
	}
	
	
	

}
