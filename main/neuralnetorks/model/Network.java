package neuralnetorks.model;

import java.util.LinkedList;

import neuralnetorks.model.layer.Layer;
import neuralnetorks.model.layer.PseudoLayer;

public class Network {
	
	private PseudoLayer inputLayer;
	private LinkedList<Layer> layers = new LinkedList<>();
	private PseudoLayer collectingLayer;
	
	public PseudoLayer getInputLayer() {
		return inputLayer;
	}
	public void setInputLayer(PseudoLayer inputLayer) {
		this.inputLayer = inputLayer;
	}
	public PseudoLayer getCollectingLayer() {
		return collectingLayer;
	}
	public void setCollectingLayer(PseudoLayer collectingLayer) {
		this.collectingLayer = collectingLayer;
	}
	public LinkedList<Layer> getLayers() {
		return layers;
	}
	
	
	

}
