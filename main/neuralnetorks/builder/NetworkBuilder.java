package neuralnetorks.builder;


import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.checklists.PreInitializationCheckList;
import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.exception.ModelBuildingException;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.AbstractLayer;
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

	private OutputLayer outputLayer;
	
	private Map<PreInitializationCheckList, Boolean> preInitCheckList;


	public NetworkBuilder(Models model) {
		this.network = new Network();
		this.network.setModel(model);
		this.preInitCheckList = Arrays.stream(PreInitializationCheckList.values())
				.collect(Collectors.toMap(value -> value, value -> false));
	}

	public Network getNetwork() {
		initialize();
		return this.network;
	}

	public void initialize() {
		
		Set<PreInitializationCheckList> misses = preInitCheckList.entrySet().stream().filter(entry -> entry.getValue()==false).map(entry -> entry.getKey()).collect(Collectors.toSet());
		if (!misses.isEmpty()) throw new ModelBuildingException(misses);
		
		if (network.getModel().equals(Models.LINEAR_REGRESSION) && network.getLayers().getLast().getNeurons().size() != 1) {
			logger.error("Cannot initialize network{}: output layer must have one neuron in linear regression model.",
					network.getName() != null ? " " + network.getName() : "");
		}
		
		/*Add output layer*/
		network.getLayers().getLast().setNext(outputLayer);
		outputLayer.setPrevious(network.getLayers().getLast());
		network.getLayers().add(outputLayer);
		

		/* Link layers */
		this.network.getLayers().forEach(layer -> {
			if (layer.getNext() != null) {
				layer.getNeurons().forEach(neuron -> 
					layer.getNext().getNeurons().forEach(nextNeuron -> linkNeurons(neuron, nextNeuron)));
			}
		});

		logger.info("Initialized network{} with {} input feauture(s) and {} output feature(s).",
				network.getName() != null ? " " + network.getName() : "", network.getLayers().getFirst().getNeurons().size(),
				network.getLayers().getLast().getNeurons().size());
		for (int i = 1; i < network.getLayers().size()-1; i++) {
			logger.info("Deep layer {} has {} neurons.", i , network.getLayers().get(i).getNeurons().size());
			for (AbstractNeuron neuron : network.getLayers().get(i).getNeurons()) {
				logger.trace("NeuronId: {}", neuron.getId());
			}
		}
		logger.info("Number of connections: {}", linksNumber);

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
			DeepLayer newLayer = new DeepLayer(numberOfNeurons, activationFunction);
			neuronCount += numberOfNeurons;
			if (!network.getLayers().isEmpty()) {
				newLayer.setPrevious(network.getLayers().getLast());
				network.getLayers().getLast().setNext(newLayer);
			}
			network.getLayers().add(newLayer);
			logger.trace("Added new layer with {} neurons and activation function {}", numberOfNeurons, activationFunction);
			return this;
	}
	
	public NetworkBuilder addInputLayer(int numberOfInputFeatures) {
		InputLayer inputLayer = new InputLayer(numberOfInputFeatures);
		if (!network.getLayers().isEmpty()) {
			network.getLayers().getFirst().setPrevious(inputLayer);
			inputLayer.setNext(network.getLayers().getFirst());
		}
		network.getLayers().addFirst(inputLayer);
		preInitCheckList.put(PreInitializationCheckList.INPUTLAYER_ADDED, true);
		logger.trace("Added input layer with {} input features", numberOfInputFeatures);
		return this;
	}
	
	public NetworkBuilder addOutputLayer(int numberOfNeurons, ActivationFunctions activationFunction) {
		/*Assegna l'output layer ad una variabile locale e lo inserisce alla fine della LinkedList
		 * durante l'inizializzazione
		 */
		this.outputLayer = new OutputLayer(numberOfNeurons, activationFunction);
		neuronCount += numberOfNeurons;
		preInitCheckList.put(PreInitializationCheckList.OUTPUTLAYER_ADDED, true);
		logger.trace("Stored output layer with {} output neurons in builder local variable", numberOfNeurons);
		return this;
	}
	
	public NetworkBuilder addOutputLayer(int numberOfNeurons) {
		return addOutputLayer(numberOfNeurons, ActivationFunctions.IDENTITY);
	}
	
	public NetworkBuilder addLayer(int numberOfNeurons) {
		return addLayer(numberOfNeurons, ActivationFunctions.IDENTITY);
	}

	public NetworkBuilder setNetworkName(String networkName) {
		network.setName(networkName);
		return this;
	}

	public void incParamsCount(int paramsCount) {
		this.paramsCount += paramsCount;
	}

}
