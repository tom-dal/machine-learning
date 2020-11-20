package neuralnetorks.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Network {
	
	protected LinkedList<Layer> layers = new LinkedList<>();
	private int inputSize;

	public LinkedList<Layer> getLayers() {
		return layers;
	}

	public void setLayers(LinkedList<Layer> layers) {
		this.layers = layers;
	}
	
	@Override
	public String toString() {
		int layerNumber = 0;
		StringBuilder builder = new StringBuilder();
		builder.append("Network [Input size=");
		builder.append(inputSize);
		for (Layer layer : layers) {
			builder.append(", layer #");
			layerNumber++;
			builder.append(layerNumber+":");
			builder.append(layer);
		}
		builder.append("]");
		return builder.toString();
	}

	public void setInputSize(int inputSize) {
		this.inputSize=inputSize;
	}
	
	public int getInputSize() {
		return this.inputSize;
	}
	
	
	
	
	

}
