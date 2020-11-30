package neuralnetorks;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.Errors;
import neuralnetorks.enums.Models;
import neuralnetorks.exception.InputCompilingException;
import neuralnetorks.function.ErrorFunction;
import neuralnetorks.function.MeanSquaredError;
import neuralnetorks.model.Link;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.Layer;
import neuralnetorks.model.neuron.AbstractNeuron;
import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.model.neuron.PseudoNeuron;
import neuralnetorks.utils.MathUtilities;

public class LearningCore {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private MathUtilities math = new MathUtilities();

	private double learningRate;
	private ErrorFunction errorFunction;
	private double diffStep = 10e-16;
	
	private boolean numericalDifferentiation = false;

	public LearningCore(double learningRate, Errors errorFunction) {
		super();
		this.learningRate = learningRate;
		configuration(errorFunction);
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}
	
	public LearningCore configuration (NetworkOptions config, boolean value) {
		if (config.equals(NetworkOptions.NUMERICAL_DIFFERENTIATION)) {
			numericalDifferentiation = value;
		}
		
		return this;
	}

	public void configuration(Errors errorFunction) {
		if (errorFunction.equals(Errors.MEAN_SQUARED_ERROR)) {
			this.errorFunction = new MeanSquaredError();
		}
	}

	public void teach(Network network, double[][] inputBatch, double[] targetBatch, int iterations) {
		
		double lastErr = 0;
		for (int i = 0; i < iterations; i++) {
			double[] outputBatch = iterateForwardPropagationOverInputBatch(network, inputBatch);
			double err = errorFunction.getError(outputBatch, targetBatch);
			if (i<10 || (((i+1)%10)==0)) {
				logger.debug("Iteration {}, current error: {}", i+1, err);
				checkForErrors(network);
			}
			backPropagate(network, inputBatch, outputBatch, targetBatch, err);
			if (err > lastErr && i!=0) {
				logger.warn("Error has increased in  iteration {}.", i+1);
			}
			lastErr = err;
		}

	}


	private void checkForErrors(Network network) {
		PseudoNeuron[] inputPn = network.getInputLayer().getPseudoNeurons();
		Arrays.stream(inputPn).forEach(pn -> {
			List<Link> links = pn.getInLinks();
			if (links.size() != 1) logger.error("Input layer has more then one input per neuron");
			if (links.get(0).getWeight()!=1) logger.error("Input link's weight different from 1 in input layer.");
		});
		Neuron[] outputNeurons = network.getLayers().getLast().getNeurons();
		if (outputNeurons.length!=1 && network.getModel().equals(Models.LINEAR_REGRESSION)) {
			logger.error("Output layer can have only one neuron in linear regression model.");
		}
		if(outputNeurons[0].getOutLinks().size()!=1) logger.error("Output layer must have one output link.");
		if(outputNeurons[0].getOutLinks().get(0).getWeight()!=1) {
			logger.error("Output link's weight different from 1 in output layer.");
		}
	}

	private double[] iterateForwardPropagationOverInputBatch(Network network, double[][] inputBatch) {
		double[] outputBatch = new double[inputBatch.length];
		for (int j = 0; j < inputBatch.length; j++) {
			compileInputLinks(network, inputBatch[j]);
			forwardPropagate(network);
			outputBatch[j] = getOutput(network);
		}
		return outputBatch;
	}

	private void compileInputLinks(Network network, double[] inputParameters) {
		if (inputParameters.length != network.getInputLayer().getPseudoNeurons().length) {
			String msg = "Number of input parameters must equal input layer size.";
			logger.error(msg);
			throw new InputCompilingException(msg);
		}

		PseudoNeuron[] pns = network.getInputLayer().getPseudoNeurons();
		for (int i = 0; i < pns.length; i++) {
			pns[i].emptyInputLinks();
			Link inLink = new Link();
			inLink.setWeight(1);
			inLink.setValue(inputParameters[i]);
			pns[i].getInLinks().add(inLink);
		}
	}

	private void forwardPropagate(Network network) {
		network.getInputLayer().process();
		network.getLayers().forEach(layer -> layer.process());

	}
	

	private void backPropagate(Network network, double[][] inputBatch, double[] outputBatch, double[] targetBatch, double err) {
		Layer currentLayer = network.getLayers().getLast();
		while (true) {
			for (Neuron neuron : currentLayer.getNeurons()) {
								
				neuron.getInLinks().forEach(inLink -> {
					double slope = calculateWeightSlope(network, inLink, inputBatch, targetBatch, err);
					logger.debug("Calculated slope for link weight {}->{}: {})", inLink.getFromNeuron().getId(), inLink.getToNeuron().getId(), slope);
					inLink.setNextWeight(inLink.getWeight() - slope*learningRate);
				});
				
				double biasSave = neuron.getBias();
				neuron.setBias(biasSave + diffStep);
				double[] newOutputBatch = iterateForwardPropagationOverInputBatch(network, inputBatch);
				double newErr = errorFunction.getError(newOutputBatch, targetBatch);
				double slope = (newErr-err)/diffStep;
				logger.debug("Calculated slope for neuron {} bias: {})", neuron.getId(), slope);
				neuron.setNextBias(biasSave - slope*learningRate); 
				neuron.setBias(biasSave);
			}
			
			if (currentLayer.getPrevious()!=null) {
				currentLayer = currentLayer.getPrevious();
			} else {
				break;
			}
		}
		currentLayer = network.getLayers().getLast();
		while (true) {
			updateLayerInlinks(currentLayer);
			if (currentLayer.getPrevious()!=null) {
				currentLayer = currentLayer.getPrevious();
			} else {
				break;
			}
		}
		
	}

	private double calculateWeightSlope(Network network, Link link, double[][] inputBatch, double[] targetBatch, double err) {
		if (numericalDifferentiation) {
			double weightSave = link.getWeight();
			link.setWeight(weightSave + diffStep);
			double[] newOutputBatch = iterateForwardPropagationOverInputBatch(network, inputBatch);
			double newErr = errorFunction.getError(newOutputBatch, targetBatch);
			double slope = (newErr - err) / diffStep;
			link.setWeight(weightSave);
			return slope;
		}
		else return 1;
	}

	private void updateLayerInlinks(Layer layer) {
		for (Neuron neuron : layer.getNeurons()) {
			neuron.updateBias();
			neuron.getInLinks().forEach(link -> link.updateWeight());
		}
	}

	private double getOutput(Network network) {
		return network.getLayers().getLast().getNeurons()[0].getOutLinks().get(0).getValue();
	}

	public double predict(Network network, double[] test) {
		compileInputLinks(network, test);
		forwardPropagate(network);
		return getOutput(network);
	}
	
		
}
