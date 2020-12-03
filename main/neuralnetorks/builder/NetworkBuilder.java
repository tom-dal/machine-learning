package neuralnetorks.builder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.Models;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.DeepLayer;
import neuralnetorks.model.link.Link;
import neuralnetorks.model.neuron.AbstractNeuron;
import neuralnetorks.utils.MathUtilities;

public class NetworkBuilder {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Network network;
	private String networkName;
	private int newLayerIndex = 0;
	int neuronCount = 0;
	int paramsCount = 0;
	int linksNumber = 0;
	private boolean inputSizeSet = false;

	private Models model;

	public NetworkBuilder(Models model) {
		this.model = model;
		if (model.equals(Models.LINEAR_REGRESSION)) {
			this.network = new Network();
		}
	}

	public Network getNetwork() {
		initialize();
		return this.network;
	}

	public NetworkBuilder initialize() {
		if (!inputSizeSet) {
			logger.error(
					"Cannot initialize network{}: input size unknown. Invoke method setInputSize() to set input size.",
					networkName != null ? " " + networkName : "");
			return this;
		}
		if (model.equals(Models.LINEAR_REGRESSION) && network.getLayers().getLast().getNeurons().size() != 1) {
			logger.error("Cannot initialize network{}: output layer must have one neuron in linear regression model.",
					networkName != null ? " " + networkName : "");
		}

		/* Link layers */
		this.network.getLayers().forEach(layer -> {
			if (layer.getNext() != null) {
				layer.getNeurons().forEach(neuron -> 
					layer.getNext().getNeurons().forEach(nextNeuron -> linkNeurons(neuron, nextNeuron)));
			}
		});

		logger.info("Initialized network{} with {} input parameter(s) and {} output parameter(s).",
				networkName != null ? " " + networkName : "", network.getLayers().getFirst().getNeurons().size(),
				network.getLayers().getLast().getNeurons().size());
		for (int i = 1; i < network.getLayers().size()-1; i++) {
			logger.info("Deep layer {} has {} neurons.", i , network.getLayers().get(i).getNeurons().size());
			for (AbstractNeuron neuron : network.getLayers().get(i).getNeurons()) {
				logger.debug("NeuronId: {}", neuron.getId());
			}
		}
		logger.info("Number of connections: {}", linksNumber);

		return this;
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

	public NetworkBuilder addDeepLayer(int numberOfNeurons) {
		DeepLayer newLayer = new DeepLayer(numberOfNeurons);
		neuronCount += numberOfNeurons;
		if (newLayerIndex != 0) {
			newLayer.setPrevious(network.getLayers().getLast());
			network.getLayers().getLast().setNext(newLayer);
		}
		this.network.getLayers().add(newLayer);
		logger.trace("Added layer {} with {} neurons", network.getLayers().indexOf(newLayer) + 1, numberOfNeurons);
		newLayerIndex++;
		return this;
	}

	public NetworkBuilder setNetworkName(String networkName) {
		this.networkName = networkName;
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
