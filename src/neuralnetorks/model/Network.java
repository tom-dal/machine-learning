package neuralnetorks.model;

import java.util.List;

public abstract class Network {
	
	protected double[] inputData;
	protected List<Layer> layers;

	public List<Layer> getLayers() {
		return layers;
	}

	public void setLayers(List<Layer> layers) {
		this.layers = layers;
	}

	public double[] getInputLayer() {
		return inputData;
	}

	public void setInputData(double[] inputData) {
		this.inputData = inputData;
	}
	
	
	

}
