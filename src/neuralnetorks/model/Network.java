package neuralnetorks.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class Network {
	
	protected double[] inputData;
	protected List<Layer> layers = new LinkedList<>();

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
	
	public void initialize(double[] inputData) {
		this.inputData = inputData;
		layers.forEach(layer -> {
			layer.getNeurons().forEach(neuron ->{
				neuron.initializeInputCouples(inputData.length);
			});
		});
	}

	@Override
	public String toString() {
		int layerNumber = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("Network [Input size=");
		builder.append(inputData.length);
		for (Layer layer : layers) {
			builder.append(", layer #");
			layerNumber++;
			builder.append(layerNumber+":");
			builder.append(layer);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
	

}
