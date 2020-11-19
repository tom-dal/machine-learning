package neuralnetorks.builder;

import neuralnetorks.function.ActivationFunction;
import neuralnetorks.model.DenseLayer;
import neuralnetorks.model.FullyConnectedNetwork;
import neuralnetorks.model.Network;

public class NetworkBuilder {
	
	protected Network network;
	
	public NetworkBuilder(){
		this.network = new FullyConnectedNetwork();
	}
	
	public Network getNetwork() {
		return this.network;
	}
	
	public NetworkBuilder addDenseLayer(int neuronsNumber) {
		this.network.getLayers().add(new DenseLayer(neuronsNumber));
		return this;
	}
	
	public NetworkBuilder addDenseLayer(int neuronsNumber, ActivationFunction activationFunction) {
		this.network.getLayers().add(new DenseLayer(neuronsNumber, activationFunction));
		return this;
	}
	
	public NetworkBuilder setInputData(double[] inputData) {
		this.network.setInputData(inputData);
		return this;
	}
	

}
