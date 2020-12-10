package neuralnetorks.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.enums.ErrorFunctions;
import neuralnetorks.enums.Models;
import neuralnetorks.enums.NetworkOptions;
import neuralnetorks.exception.InputCompilingException;
import neuralnetorks.function.ErrorFunctionInterface;
import neuralnetorks.function.MeanSquaredError;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.AbstractLayer;
import neuralnetorks.model.layer.DeepLayer;
import neuralnetorks.model.layer.InputLayer;
import neuralnetorks.model.layer.OutputLayer;
import neuralnetorks.model.link.AbstractLink;
import neuralnetorks.model.link.Link;
import neuralnetorks.model.neuron.AbstractNeuron;
import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.model.neuron.OutputNeuron;
import neuralnetorks.model.neuron.InputNeuron;
import neuralnetorks.utils.MathUtilities;

public class LearningCore {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Network network;
	private double[][] inputBatch;
	private double[][] targetBatch;
	private double lastErr = 0;
	private int iterationCount;

	private double learningRate;
	private ErrorFunctionInterface errorFunction;
	private double initialDiffStep;
	private int inputBatchLength;
	private double[] inputMin;
	private double[] inputSpan;
	private double[] inputAverage;
	private double[] targetAverage;
	private double[] targetMin;
	private double[] targetSpan;
	private boolean inputNormalized = false;
	private boolean targetNormalized = false;

	static int index = 0;

	private Map<NetworkOptions, Boolean> options;

	public LearningCore(Network network) {
		super();
		this.network = network;
		/* SET ALL OPTIONS TO FALSE */
		this.options = Arrays.stream(NetworkOptions.values())
				.collect(Collectors.toMap(option -> option, option -> false));
		/* DEFAULT CONFIGURATIONS */
		this.learningRate = 0.001;
		configuration(ErrorFunctions.MEAN_SQUARED_ERROR);
		this.initialDiffStep = 10e-15;
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

	public void learn(double[][] inputData, double[][] targetData, int iterations) {

		this.inputBatch = MathUtilities.copy2dArray(inputData);
		this.targetBatch = MathUtilities.copy2dArray(targetData);

		inputBatchLength = inputData.length;
		int inputFeatures = inputData[0].length;
		int outputFeatures = targetData[0].length;

		this.inputMin = new double[inputFeatures];
		this.inputSpan = new double[inputFeatures];
		this.targetMin = new double[outputFeatures];
		this.targetSpan = new double[outputFeatures];

		for (int i = 0; i < inputFeatures; i++) {
			index=i;
			inputMin[index] = Arrays.stream(inputBatch).mapToDouble(array -> array[index]).min().getAsDouble();
			inputSpan[index] = Arrays.stream(inputBatch).mapToDouble(array -> array[index]).max().getAsDouble() - inputMin[index];
		}
		
		for (int i = 0; i < outputFeatures; i++) {
			index=i;
			targetMin[index] = Arrays.stream(targetBatch).mapToDouble(array -> array[index]).min().getAsDouble();
			targetSpan[index] = Arrays.stream(targetBatch).mapToDouble(array -> array[index]).max().getAsDouble() - targetMin[index];
		}

		if (options.get(NetworkOptions.INPUT_BATCH_CENTERING)) {
			inputAverage = MathUtilities.average(inputBatch);
			MathUtilities.center(inputBatch, inputAverage); /* da testare */
		}
		if (options.get(NetworkOptions.TARGET_BATCH_CENTERING)) {
			targetAverage = MathUtilities.average(targetBatch);
			MathUtilities.center(targetBatch, targetAverage);
		}
		if (options.get(NetworkOptions.INPUT_BATCH_NORMALIZATION)) {
			MathUtilities.normalizeBatch(inputBatch, inputSpan, inputMin);
			inputNormalized = true;
		}
		if (options.get(NetworkOptions.TARGET_BATCH_NORMALIZATION)) {
			MathUtilities.normalizeBatch(targetBatch, targetSpan, targetMin);
			targetNormalized = true;
		}

		setLinkValueBatchSize(inputBatchLength);

		iterationCount = 0;
		for (int i = 0; i < iterations; i++) {
			double[][] outputBatch = iterateForwardPropagationOverInputBatch();
			double err = errorFunction.getError(outputBatch, targetData);
			if (err > lastErr && i != 0) {
				logger.warn("Error has increased in last iteration {}.", i + 1);
				initialDiffStep *= 10;
			}
			lastErr = err;
			if (i < 10 || (((i + 1) % 10) == 0)) {
				checkForErrors();
			}
			logger.debug("Iteration {}, current error: {}", i + 1, err);
			backPropagate(err);
		}
		iterationCount += iterations;
	}

	public void learn(int iterations) {

		for (int i = iterationCount; i < iterationCount + iterations; i++) {
			double[][] outputBatch = iterateForwardPropagationOverInputBatch();
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
			backPropagate(err);
		}
		iterationCount += iterations;
	}

	private void setLinkValueBatchSize(int length) {
		network.getLayers().stream().filter(layer -> !layer.getClass().equals(OutputLayer.class))
		.forEach(layer -> layer.getNeurons()
				.forEach(neuron -> neuron.getOutLinks().forEach(link -> link.setValueBatch(new double[length]))));
		network.getOutputLayer().getNeurons().forEach(neuron -> ((OutputNeuron) neuron).getOutLink().setValueBatch(new double[length]));
	}

	private void checkForErrors() {
		Set<AbstractNeuron> outputNeurons = network.getLayers().getLast().getNeurons();
		if (outputNeurons.size() != 1 && network.getModel().equals(Models.LINEAR_REGRESSION)) {
			logger.error("Output layer can have only one neuron in linear regression model.");
		}
	}

	private double[][] iterateForwardPropagationOverInputBatch() {
		double[][] outputBatch = new double[inputBatchLength][];
		for (int j = 0; j < inputBatchLength; j++) {
			compileInputLinks(inputBatch[j]);
			forwardPropagate(j);
			outputBatch[j] = getOutput();
		}
		return outputBatch;
	}

	private void compileInputLinks(double[] inputValues) throws InputCompilingException {

		Set<AbstractNeuron> inputNeurons = network.getInputLayer().getNeurons();
		if (inputValues.length != inputNeurons.size()) {
			throw new InputCompilingException("Number of input parameters must equal neurons number in input layer.");
		}

		index = 0;
		inputNeurons.forEach(neuron -> {
			InputNeuron inputNeuron = (InputNeuron) neuron;
			inputNeuron.getInLink().setValue(inputValues[index]);
			index++;
		});
	}

	private void forwardPropagate(int batchIndex) {
		network.getInputLayer().process(batchIndex);
		network.getLayers().forEach(layer -> layer.process(batchIndex));

	}

	private void forwardPropagate() {
		network.getInputLayer().process();
		network.getLayers().forEach(layer -> layer.process());

	}

	private void backPropagate(double err) {
		AbstractLayer currentLayer = network.getLayers().getLast();
		while (currentLayer.getPrevious() != null) {
			currentLayer.getNeurons().forEach(neuron -> {

				neuron.getInLinks().forEach(inLink -> {
					double slope = calculateWeightSlope(inLink, err);
					logger.debug("Calculated error/weight slope for link  {} --> {}:  {})", inLink.getFromNeuron().getId(),
							inLink.getToNeuron().getId(), slope);
					inLink.setNextWeight(inLink.getWeight() - slope * learningRate);
				});

				double slope = calculateBiasSlope(neuron, err);
				logger.debug("Calculated error/bias slope for neuron {}:  {})", neuron.getId(), slope);
				neuron.setNextBias(neuron.getBias() - slope * learningRate);
			});

			currentLayer = currentLayer.getPrevious();
		}
		
		/*UPDATE WEIGHTS*/
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
				double[][] newOutputBatch = iterateForwardPropagationOverInputBatch();
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
			double slope = Math.sqrt(err * inputBatchLength) / inputBatchLength;
			double propagationFactor = getPropagationFactor(neuron);
			slope *= propagationFactor;
			slope *= inputBatchLength;
			logger.debug("Neuron {} propagation factor: {}", neuron.getId(), propagationFactor);
			return slope;
		}
	}

	public double getPropagationFactor(AbstractNeuron neuron) {
		if (neuron.getLayer().getNext() != null) {
			AbstractLink callerLink = new Link();/* Serve solo per il debug */
			double[] factors = recursivePathExplorer(neuron, 1, 0, callerLink);
			return factors[1];
		} else
			return 1;
	}

	private double[] recursivePathExplorer(AbstractNeuron neuron, double singlePathFactor, double cumulativeFactor,
			AbstractLink callerLink) {
		double[] factors = { singlePathFactor, cumulativeFactor };
		for (AbstractLink link : neuron.getOutLinks()) {
			logger.trace("Layer {}, Neuron {}, current single path factor: {}, cumulative factor {}",
					neuron.getLayer().getId(), neuron.getId(), factors[0], factors[1]);
			factors[0] *= link.getWeight();
			logger.trace("Link {} -> {}: multypling SPF for link weight {}. New Value: {}", neuron.getId(),
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

	private Double calculateWeightSlope(AbstractLink link, double err) {
		if (options.get(NetworkOptions.NUMERICAL_DIFFERENTIATION)) {
			double weightSave = link.getWeight();
			double diffStep = initialDiffStep;
			double newErr;
			while (true) {
				link.setWeight(weightSave + diffStep);
				double[][] newOutputBatch = iterateForwardPropagationOverInputBatch();
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
			double slope = Math.sqrt(err * inputBatchLength) / inputBatchLength;
			double propagationFactor = getPropagationFactor(link.getToNeuron());
			slope *= propagationFactor;
			slope *= Arrays.stream(link.getValueBatch()).sum();
			return slope;
		}
	}

	private void updateLayerInlinks(AbstractLayer layer) {
		layer.getNeurons().forEach(neuron -> {
			if (neuron.getInLinks() != null) { /* InputNeuron ritorna null e non ho bisogno di aggiornarlo */
				neuron.updateBias();
				neuron.getInLinks().forEach(link -> link.updateWeight());
			}
		});
	}

	private double[] getOutput() {
		return network.getLayers().getLast().getNeurons().stream()
				.mapToDouble(neuron -> ((OutputNeuron) neuron).getOutLink().getValue()).toArray();
	}

	public double[] predict(double[] input) {
		double[] copyInput = Arrays.copyOf(input, input.length);
		if (options.get(NetworkOptions.INPUT_BATCH_CENTERING)) {
			MathUtilities.centerSingleInput(copyInput, inputAverage);
		}
		if (options.get(NetworkOptions.INPUT_BATCH_NORMALIZATION))
			 MathUtilities.normalizeSingleInput(copyInput, inputMin, inputSpan);
		compileInputLinks(copyInput);
		forwardPropagate();
		double[] output = getOutput();
		if (options.get(NetworkOptions.TARGET_BATCH_NORMALIZATION))
			MathUtilities.revertNormalization(output, inputMin, inputSpan );
		if (options.get(NetworkOptions.TARGET_BATCH_CENTERING))
			MathUtilities.revertCentering(output, inputAverage);
		return output;
	}

}
