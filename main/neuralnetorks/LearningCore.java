package neuralnetorks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.exception.InputCompilingException;
import neuralnetorks.function.ErrorFunctionInterface;
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

	private Network network;
	private double[][] inputBatch;
	private double[] targetBatch;
	private double lastErr = 0;
	private int iterationCount;

	private double learningRate;
	private ErrorFunctionInterface errorFunction;
	private double initialDiffStep;
	private double inputMin;
	private double inputSpan;
	private double targetMin;
	private double targetSpan;

	private Map<NetworkOptions, Boolean> options;

	private boolean newDataSet = true;
	private boolean inputNormalized = false;
	private boolean targetNormalized = false;

	public LearningCore(Network network) {
		super();
		this.network = network;
		/* SET ALL OPTIONS TO FALSE */
		this.options = Arrays.stream(NetworkOptions.values())
				.collect(Collectors.toMap(option -> option, option -> false));
		/* DEFAULT CONFIGURATIONS */
		this.learningRate = 0.001;
		configuration(ErrorFunctions.MEAN_SQUARED_ERROR);
		this.initialDiffStep = 10e-10;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public void setInitialDiffStep(double step) {
		this.initialDiffStep = step;
	}

	public LearningCore configuration(NetworkOptions option, boolean value) {
		Boolean check = options.put(option, value);
		if (check == null)
			logger.warn("No configuration option named {} has been found.", option.toString());
		return this;
	}

	public void configuration(ErrorFunctions errorFunction) {
		if (errorFunction.equals(ErrorFunctions.MEAN_SQUARED_ERROR)) {
			this.errorFunction = new MeanSquaredError();
		}
	}

	public void learn(double[][] inputBatch, double[] targetBatch, int iterations) {

		double[][] inputCopy = Arrays.copyOf(inputBatch, inputBatch.length);
		double[] targetCopy = Arrays.copyOf(targetBatch, targetBatch.length);

		if (inputCopy != this.inputBatch || targetCopy != this.targetBatch)
			newDataSet = true;

		if (newDataSet) {
			this.inputBatch = inputCopy;
			this.targetBatch = targetCopy;
			newDataSet = false;

			if (!inputNormalized)
				this.inputMin = Arrays.stream(inputBatch).mapToDouble(i -> i[0]).min().getAsDouble();
			if (!inputNormalized)
				this.inputSpan = Arrays.stream(inputBatch).mapToDouble(i -> i[0]).max().getAsDouble() - inputMin;
			if (!targetNormalized)
				this.targetMin = Arrays.stream(targetBatch).min().getAsDouble();
			if (!targetNormalized)
				this.targetSpan = Arrays.stream(targetBatch).max().getAsDouble() - targetMin;
		}

		if (options.get(NetworkOptions.INPUT_BATCH_NORMALIZATION)) {
			inputBatch = MathUtilities.normalize(inputBatch);
			inputNormalized = true;
		}
		if (options.get(NetworkOptions.TARGET_BATCH_NORMALIZATION)) {
			targetBatch = MathUtilities.normalize(targetBatch);
			targetNormalized = true;
		}

		setLinkValueBatchSize(inputBatch.length);

		for (int i = 0; i < iterations; i++) {
			iterationCount++;
			double[] outputBatch = iterateForwardPropagationOverInputBatch();
			double err = errorFunction.getError(outputBatch, targetBatch);
			if (err > lastErr && i != 0) {
				logger.warn("Error has increased in last iteration {}.", i + 1);
				initialDiffStep *= 10;
			}
			lastErr = err;
			if (i < 10 || (((i + 1) % 10) == 0)) {
				checkForErrors();
			}
			logger.debug("Iteration {}, current error: {}", i + 1, err);
			backPropagate(outputBatch, err);
			
		}

	}

	public void learn(int iterations) {

		for (int i = iterationCount; i < iterationCount + iterations; i++) {
			double[] outputBatch = iterateForwardPropagationOverInputBatch();
			double err = errorFunction.getError(outputBatch, targetBatch);
			if (err > lastErr && i != 0) {
				logger.warn("Error has increased in last iteration {}.", i + 1);
				initialDiffStep *= 10;
			}
			lastErr = err;
			if (i < 10 || (((i + 1) % 10) == 0)) {
				checkForErrors();
			}
			logger.debug("Iteration {}, current error: {}", i + 1, err);
			backPropagate(outputBatch, err);
			
		}

	}

	private void setLinkValueBatchSize(int length) {
		Arrays.stream(network.getInputLayer().getPseudoNeurons())
				.forEach(neuron -> neuron.getOutLinks().forEach(link -> link.setValueBatch(new double[length])));
		network.getLayers().forEach(layer -> Arrays.stream(layer.getNeurons())
				.forEach(neuron -> neuron.getOutLinks().forEach(link -> link.setValueBatch(new double[length]))));
	}

	private void checkForErrors() {
		PseudoNeuron[] inputPn = network.getInputLayer().getPseudoNeurons();
		Arrays.stream(inputPn).forEach(pn -> {
			List<Link> links = pn.getInLinks();
			if (links.size() != 1)
				logger.error("Input layer has more then one input per neuron");
			if (links.get(0).getWeight() != 1)
				logger.error("Input link's weight different from 1 in input layer.");
		});
		Neuron[] outputNeurons = network.getLayers().getLast().getNeurons();
		if (outputNeurons.length != 1 && network.getModel().equals(Models.LINEAR_REGRESSION)) {
			logger.error("Output layer can have only one neuron in linear regression model.");
		}
		if (outputNeurons[0].getOutLinks().size() != 1)
			logger.error("Output layer must have one output link.");
		if (outputNeurons[0].getOutLinks().get(0).getWeight() != 1) {
			logger.error("Output link's weight different from 1 in output layer.");
		}
	}

	private double[] iterateForwardPropagationOverInputBatch() {
		double[] outputBatch = new double[inputBatch.length];
		for (int j = 0; j < inputBatch.length; j++) {
			compileInputLinks(inputBatch[j]);
			forwardPropagate(j);
			outputBatch[j] = getOutput();
		}
		return outputBatch;
	}

	private void compileInputLinks(double[] inputParameters) {
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

	private void forwardPropagate(int batchIndex) {
		network.getInputLayer().process(batchIndex);
		network.getLayers().forEach(layer -> layer.process(batchIndex));

	}

	private void forwardPropagate() {
		network.getInputLayer().process();
		network.getLayers().forEach(layer -> layer.process());

	}

	private void backPropagate(double[] outputBatch, double err) {
		Layer currentLayer = network.getLayers().getLast();
		while (true) {
			for (Neuron neuron : currentLayer.getNeurons()) {

				neuron.getInLinks().forEach(inLink -> {
					double slope = calculateWeightSlope(inLink, err);
					logger.debug("Calculated error/weight slope for link  {}->{}: {})", inLink.getFromNeuron().getId(),
							inLink.getToNeuron().getId(), slope);
					inLink.setNextWeight(inLink.getWeight() - slope * learningRate);
				});

				double slope = calculateBiasSlope(neuron, err);
				logger.debug("Calculated error/bias slope for neuron {}: {})", neuron.getId(), slope);
				neuron.setNextBias(neuron.getBias() - slope * learningRate);
			}

			if (currentLayer.getPrevious() != null) {
				currentLayer = currentLayer.getPrevious();
			} else {
				break;
			}
		}
		currentLayer = network.getLayers().getLast();
		while (true) {
			updateLayerInlinks(currentLayer);
			if (currentLayer.getPrevious() != null) {
				currentLayer = currentLayer.getPrevious();
			} else {
				break;
			}
		}

	}

	private Double calculateBiasSlope(AbstractNeuron neuron, double err) {
		if (options.get(NetworkOptions.NUMERICAL_DIFFERENTIATION)) {
			double biasSave = neuron.getBias();
			double diffStep = initialDiffStep;
			double newErr;
			while (true) {
				neuron.setBias(biasSave + diffStep);
				double[] newOutputBatch = iterateForwardPropagationOverInputBatch();
				newErr = errorFunction.getError(newOutputBatch, targetBatch);
				if (newErr == err) {
					diffStep *= 10;
				} else {
					break;
				}
			}
			double slope = (newErr - err) / diffStep;
			return slope;
		} else {
			double slope = Math.sqrt(err * inputBatch.length) / inputBatch.length;
			double propagationFactor = getPropagationFactor(neuron);
			slope *= propagationFactor;
			slope *= inputBatch.length;
			logger.debug("Neuron {} propagation factor: {}", neuron.getId(), propagationFactor);
			return slope;
		}
	}

	public double getPropagationFactor(AbstractNeuron neuron) {
		if (neuron.getLayer().getNext() != null) {
			Link callerLink = new Link();/* Serve solo per il debug */
			double[] factors = recursivePathExplorer(neuron, 1, 0, callerLink);
			return factors[1];
		} else
			return 1;
	}

	private double[] recursivePathExplorer(AbstractNeuron neuron, double singlePathFactor, double cumulativeFactor,
			Link callerLink) {
		double[] factors = { singlePathFactor, cumulativeFactor };
		for (Link link : neuron.getOutLinks()) {
			logger.trace("Layer {}, Neuron {}, current single path factor: {}, cumulative factor {}",
					neuron.getLayer().getId(), neuron.getId(), factors[0], factors[1]);
			factors[0] *= link.getWeight();
			logger.trace("Link {} -> {}: multypling spf for link weight {}. New Value: {}", neuron.getId(),
					link.getToNeuron().getId(), link.getWeight(), factors[0]);

			if (link.getToNeuron().getLayer().getNext() != null) {
				logger.trace("Going from neuron {} to neuron {}", neuron.getId(), link.getToNeuron().getId());
				factors = recursivePathExplorer(link.getToNeuron(), factors[0], factors[1], link);
			} else {
				factors[1] += factors[0];
				logger.trace("Adding SPF to cumulative factor... new value: {}", factors[1]);
			}

			factors[0] /= link.getWeight();
			logger.trace("Going back to neuron {}... SPF restored to previous value {}", link.getFromNeuron().getId(),
					factors[0]);

		}
		if (callerLink.getFromNeuron() == null) {
			logger.trace("All paths calculated. Propagation factor {}", factors[1]);
			logger.trace("Back to caller method...");
		}
		return factors;
	}

	private Double calculateWeightSlope(Link link, double err) {
		if (options.get(NetworkOptions.NUMERICAL_DIFFERENTIATION)) {
			double weightSave = link.getWeight();
			double diffStep = initialDiffStep;
			double newErr;
			while (true) {
				link.setWeight(weightSave + diffStep);
				double[] newOutputBatch = iterateForwardPropagationOverInputBatch();
				newErr = errorFunction.getError(newOutputBatch, targetBatch);
				if (newErr == err) {
					diffStep *= 10;
				} else {
					break;
				}
			}
			double slope = (newErr - err) / diffStep;
			link.setWeight(weightSave);
			return slope;
		} else {
			double slope = Math.sqrt(err * inputBatch.length) / inputBatch.length;
			double propagationFactor = getPropagationFactor(link.getToNeuron());
			slope *= propagationFactor;
			slope *= Arrays.stream(link.getValueBatch()).sum();
			return slope;
		}
	}

	private void updateLayerInlinks(Layer layer) {
		for (Neuron neuron : layer.getNeurons()) {
			neuron.updateBias();
			neuron.getInLinks().forEach(link -> link.updateWeight());
		}
	}

	private double getOutput() {
		return network.getLayers().getLast().getNeurons()[0].getOutLinks().get(0).getValue();
	}

	public double predict(double[] input) {
		double[] copyInput = Arrays.stream(input).map(d -> d).toArray();
		if (options.get(NetworkOptions.INPUT_BATCH_NORMALIZATION))
			copyInput = MathUtilities.normalize(copyInput, inputMin, inputSpan);
		compileInputLinks(copyInput);
		forwardPropagate();
		double output = getOutput();
		if (options.get(NetworkOptions.TARGET_BATCH_NORMALIZATION))
			output = (targetMin + output * targetSpan);
		return output;
	}

}
