package neuralnetorks.builder;



import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.Models;
import neuralnetorks.model.Link;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.Layer;
import neuralnetorks.model.layer.PseudoLayer;
import neuralnetorks.model.neuron.AbstractNeuron;
import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.model.neuron.PseudoNeuron;
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
	
	public NetworkBuilder(Models model){
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
			logger.error("Cannot initialize network{}: input size unknown. Invoke method setInputSize() to set input size.", networkName!=null? " "+networkName:"");
			return this;
		}
		if (model.equals(Models.LINEAR_REGRESSION) && network.getLayers().getLast().getNeurons().length!=1) {
			logger.error("Cannot initialize network{}: output layer must have one neuron in linear regression model.", networkName!=null? " "+networkName:"");
		} 
		
			this.network.setModel(model);
			/*Link input layer to first layer*/
			for (PseudoNeuron pn : this.network.getInputLayer().getPseudoNeurons()) {
				Arrays.stream(this.network.getLayers().getFirst().getNeurons()).forEach(
						neuron -> linkNeurons(pn, neuron));
			}
			/*Link deep layers*/
			this.network.getLayers().forEach(layer -> {
				if (layer.getNext()!=null) {
					for (Neuron neuron : layer.getNeurons()) {
						Arrays.stream(layer.getNext().getNeurons()).forEach(
								nextNeuron -> linkNeurons(neuron, nextNeuron));
					}
				} else {
					setOutputLink(layer);
				}
			});

		
			
			logger.info("Initialized network{} with {} input parameter(s) and {} output parameter(s).",
					networkName!=null? " "+networkName:"",
					network.getInputLayer().getPseudoNeurons().length,
					network.getLayers().getLast().getNeurons().length);
			for (int i = 0;  i< newLayerIndex-1; i++) {
				logger.info("Hidden layer {} has {} neurons.", i+1, network.getLayers().get(i).getNeurons().length);
				for (Neuron neuron : network.getLayers().get(i).getNeurons()) {
					logger.debug("NeuronId: {}", neuron.getId());
				}
			}
			logger.info("Number of connections: {}", linksNumber);

		return this;
	}
	
	private void setOutputLink(Layer layer) {
		Link outLink = new Link();
		Neuron fromNeuron = layer.getNeurons()[0];
		PseudoNeuron toNeuron = new PseudoNeuron();
		outLink.setFromNeuron(fromNeuron);
		outLink.setToNeuron(toNeuron);
		outLink.setWeight(1);
		fromNeuron.getOutLinks().add(outLink);
		toNeuron.getInLinks().add(outLink);
		linksNumber++;
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

	public NetworkBuilder addLayer(int neuronsNumber) {
		Layer newLayer = new Layer(neuronsNumber);
		neuronCount+=neuronsNumber;
		if(newLayerIndex!=0) {
			newLayer.setPrevious(network.getLayers().getLast());
			network.getLayers().getLast().setNext(newLayer);
		}
		this.network.getLayers().add(newLayer);
		logger.trace("Added layer {} with {} neurons", network.getLayers().indexOf(newLayer)+1, neuronsNumber);
		newLayerIndex++;
		return this;
	}
	
	public NetworkBuilder setNetworkName(String networkName) {
		this.networkName = networkName;
		return this;
	}

	public NetworkBuilder setInputSize(int inputSize) {
		this.network.setInputLayer(new PseudoLayer(inputSize));
		this.inputSizeSet = true;
		return this;
	}
	
	public void incParamsCount(int paramsCount) {
		this.paramsCount += paramsCount;
	}
	

}
