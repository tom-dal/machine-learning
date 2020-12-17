package neuralnetorks.builder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.DeepLayer;
import neuralnetorks.model.layer.InputLayer;
import neuralnetorks.model.layer.OutputLayer;
import neuralnetorks.model.link.Link;
import neuralnetorks.model.neuron.AbstractNeuron;
import neuralnetorks.utils.MathUtilities;

public class NetworkBuilder {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Network network;
	
	private int newLayerIndex = 0;
	int neuronCount = 0;
	int paramsCount = 0;
	int linksNumber = 0;
	private boolean inputSizeSet = false;


	public NetworkBuilder(Models model) {
		this.network = new Network();
		this.network.setModel(model);
	}

	public Network getNetwork() {
		initialize();
		return this.network;
	}

	public NetworkBuilder initialize() {
		if (!inputSizeSet) {
			logger.error(
					"Cannot initialize network{}: input size unknown. Invoke method setInputSize() to set input size.",
					network.getName() != null ? " " + network.getName() : "");
			return this;
		}
		if (network.getModel().equals(Models.LINEAR_REGRESSION) && network.getLayers().getLast().getNeurons().size() != 1) {
			logger.error("Cannot initialize network{}: output layer must have one neuron in linear regression model.",
					network.getName() != null ? " " + network.getName() : "");
		}
		
		convertLastLayerToOutput();

		/* Link layers */
		this.network.getLayers().forEach(layer -> {
			if (layer.getNext() != null) {
				layer.getNeurons().forEach(neuron -> 
					layer.getNext().getNeurons().forEach(nextNeuron -> linkNeurons(neuron, nextNeuron)));
			}
		});

		logger.info("Initialized network{} with {} input parameter(s) and {} output parameter(s).",
				network.getName() != null ? " " + network.getName() : "", network.getLayers().getFirst().getNeurons().size(),
				network.getLayers().getLast().getNeurons().size());
		for (int i = 1; i < network.getLayers().size()-1; i++) {
			logger.info("Deep layer {} has {} neurons.", i , network.getLayers().get(i).getNeurons().size());
			for (AbstractNeuron neuron : network.getLayers().get(i).getNeurons()) {
				logger.trace("NeuronId: {}", neuron.getId());
			}
		}
		logger.info("Number of connections: {}", linksNumber);

		return this;
	}


	private void convertLastLayerToOutput() {
		int neuronsNumber = network.getLayers().getLast().getNeurons().size();
		ActivationFunctions activationFunction = Enum.valueOf(ActivationFunctions.class, 
				network.getLayers().getLast().getNeurons().stream().findAny().get().getActivationFunction()
				.getClass().getSimpleName().toUpperCase());
		network.getLayers().removeLast();
		OutputLayer outputLayer = new OutputLayer(neuronsNumber, activationFunction);
		outputLayer.setPrevious(network.getLayers().getLast());
		network.getLayers().getLast().setNext(outputLayer);
		network.getLayers().add(outputLayer);
	}

	private void linkNeurons(AbstractNeuron fromNeuron, AbstractNeuron toNeuron) {
		Link link = new Link();
		link.setFromNeuron(fromNeuron);
		link.setToNeuron(toNeuron);
		link.setValue(0);
		link.setWeight(MathUtilities.getRandomDouble());
		fromNeuron.getOutLinks().add(link);
		toNeuron.getInLinks().add(link);
		linksNumber++;
	}

	public NetworkBuilder addLayer(int numberOfNeurons, ActivationFunctions activationFunction) {
		if (!network.getLayers().isEmpty() && newLayerIndex != 0) /*E' un double check*/ { 
			DeepLayer newLayer = new DeepLayer(numberOfNeurons, activationFunction);
			neuronCount += numberOfNeurons;
			newLayer.setPrevious(network.getLayers().getLast());
			network.getLayers().getLast().setNext(newLayer);
			this.network.getLayers().add(newLayer);
			newLayerIndex++;
			logger.trace("Added layer {} with {} neurons", network.getLayers().indexOf(newLayer) + 1, numberOfNeurons);
			return this;
		} else {
			InputLayer inputLayer = new InputLayer(numberOfNeurons);
			neuronCount += numberOfNeurons;
			this.network.getLayers().add(inputLayer);
			newLayerIndex++;
			logger.trace("Added input layer with {} neurons", numberOfNeurons);
			return this;
		}
	}
	
	public NetworkBuilder addLayer(int numberOfNeurons) {
		return addLayer(numberOfNeurons, ActivationFunctions.IDENTITY);
	}

	public NetworkBuilder setNetworkName(String networkName) {
		network.setName(networkName);
		return this;
	}

	public NetworkBuilder setInputSize(int inputSize) {
		this.inputSizeSet = true;
		return this;
	}

	public void incParamsCount(int paramsCount) {
		this.paramsCount += paramsCount;
	}

}
