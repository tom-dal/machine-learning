package neuralnetorks.builder;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.model.DenseLayer;
import neuralnetorks.model.FullyConnectedNetwork;
import neuralnetorks.model.Network;

public class NetworkBuilder {
	
	private Network network;
	private int newLayerIndex = 0;
	
	public NetworkBuilder(){
		this.network = new FullyConnectedNetwork();
	}
	
	public Network getNetwork() {
		return this.network;
	}
	
	public NetworkBuilder initialize() {
		this.network.getLayers().forEach(layer -> {
									if(layer.getPrevious()!=null) {
										int previousNeuronsNumber = layer.getPrevious().getNeurons().size();
										layer.getNeurons().forEach(neuron -> {
											neuron.setRandomWeights(previousNeuronsNumber);
											neuron.setRandomBias();
										});
									}
								});
		return this;
	}
	
	public NetworkBuilder addDenseLayer(int neuronsNumber) {
		DenseLayer newLayer = new DenseLayer(neuronsNumber);
		if(newLayerIndex!=0) {
			newLayer.setPrevious(network.getLayers().getLast());
			network.getLayers().getLast().setNext(newLayer);
		}
		this.network.getLayers().add(newLayer);
		return this;
	}
	
	public NetworkBuilder addDenseLayer(int neuronsNumber, ActivationFunction activationFunction) {
		this.network.getLayers().add(new DenseLayer(neuronsNumber, activationFunction));
		return this;
	}
	
	public NetworkBuilder setInputSize(int inputSize) {
		this.network.setInputSize(inputSize);
		return this;
	}
	

}
