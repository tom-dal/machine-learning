package neuralnetorks;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.function.MeanSquaredError;
import neuralnetorks.model.Network;
import neuralnetorks.model.layer.AbstractLayer;
import neuralnetorks.model.neuron.Neuron;
import neuralnetorks.utils.IdUtil;

public class LearningCore {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private double learningRate;
	private MeanSquaredError errorFunction;
	private final double diffStep = 0.000001;
	private Map<Long, Double> slopes; 

	public enum ErrorFunctions {
		MEAN_SQUARED_ERROR;
	}

	public void setLearningRate(double lr) {
		this.learningRate = lr;
	}

	public void setErrorFunction(ErrorFunctions function) {
		if (function.equals(ErrorFunctions.MEAN_SQUARED_ERROR)) {
			this.errorFunction = new MeanSquaredError();
		}
	}

//	public void teach(Network fcn, double[][] inputData, double[] targetData, int iterations) {
//		
//		for (int i = 0; i < iterations; i++) {
//			double[][] predicted = processNetwork(fcn, inputData);
//			double mse = errorFunction.getError(predicted , targetData);
//			logger.debug("Iteation #{}, differentiating error function.",i);
//			Map<Long, Double> slopes = differentiateError(fcn, inputData, mse, targetData);
//			updateNetWork(fcn, slopes);
//			for (int j=0; j<predicted.length;j++) {
//				logger.debug("Predicted: {}, Actual: {}",predicted[i][0],targetData[j] );
//			}
//		}
//		
//	}

	public void teach(Network fcn, double[][] inputData, double[] targetData, int iterations) {

		for (int i = 0; i < iterations; i++) {
			double[][] predicted = processNetwork(fcn, inputData);
			double mse = errorFunction.getError(predicted, targetData);
			backPropagate(fcn,predicted, targetData, mse);
			updateNetWork(fcn, slopes);
			for (int j = 0; j < predicted.length; j++) {
				logger.debug("Predicted: {}, Actual: {}", predicted[i][0], targetData[j]);
			}
		}

	}

	private void backPropagate(Network fcn, double[][] predicted, double[] targetData, double mse) {
		AbstractLayer layer = fcn.getLayers().getLast();
		while (layer.getPrevious()!=null) {
			layer.getNeurons().forEach((neuronHash,neuron) -> {
				neuron.getWeights().forEach((weightHash,weight) -> {
					
				});
			});
			
		}
		
	}

	private void updateNetWork(Network network, Map<Long, Double> slopes) {
		slopes.entrySet().forEach(entry -> {
			network.getLayers().forEach(layer -> {
				Neuron neuron = layer.getNeurons().get(entry.getKey());
				if (neuron != null) {
					neuron.setBias(neuron.getBias() - learningRate * entry.getValue());
				} else {
					layer.getNeurons().forEach((neuronHash, notNullNeuron) -> {
						notNullNeuron.getWeights().forEach((weightHash, weight) -> {
							notNullNeuron.getWeights().put(weightHash, weight -= learningRate * entry.getValue());
						});
					});
				};
			});
		});
	}

	private Map<Long, Double> differentiateError(Network network, double[][] inputData, double mse,
			double[] targetData) {
		Map<Long, Double> slopes = new LinkedHashMap<>();
		Network clonedNetwork = cloneNetwork(network);
		clonedNetwork.getLayers().forEach(layer -> layer.getNeurons().forEach((neuronHash, neuron) -> {
			neuron.getWeights().forEach((weightHash, weight) -> {
				double weightSave = weight;
				double[][] d = processNetwork(clonedNetwork, inputData);
				logger.debug("Output: {}", d[0][0]);
				neuron.getWeights().put(weightHash, weight += diffStep);
				double[][] predicted = processNetwork(clonedNetwork, inputData);
				logger.debug("Output after increment: {}", predicted[0][0]);
				double newMse = errorFunction.getError(predicted, targetData);
				double slope = (newMse - mse) / diffStep;
				logger.debug("Mse: old={} new={} difference={}", mse, newMse, newMse - mse);
				slopes.put(weightHash, slope);
				neuron.getWeights().put(weightHash, weightSave);
			});
			double biasSave = neuron.getBias();
			neuron.setBias(biasSave + diffStep);
			double[][] predicted = processNetwork(clonedNetwork, inputData);
			double newMse = errorFunction.getError(predicted, targetData);
			double slope = (newMse - mse) / diffStep;
			slopes.put(neuronHash, slope); /*
											 * I bias sono identifcati con l'hash del neurone perchè ce n'è solo uno per
											 * neurone
											 */
			neuron.setBias(biasSave);
		}));
		return slopes;
	}



	private double[][] processNetwork(Network network, double[][] inputData) {
		double[][] predicted = new double[inputData.length][];
		for (int i = 0; i < inputData.length; i++) {
			double[] iterativeIO = inputData[i];
			for (AbstractLayer layer : network.getLayers()) {
				iterativeIO = layer.process(iterativeIO);
			}
			if (iterativeIO.length == 1) {
				predicted[i] = iterativeIO;
			} else {
				logger.error("Last layer's output is not a single number");
			}
		}
		return predicted;
	}

	public Network cloneNetwork(Network network) {
		NetworkBuilder builder = new NetworkBuilder();
		Network clonedNetwork = builder.getNetwork();

		clonedNetwork.setInputSize(network.getInputSize());
		network.getLayers().forEach(layer -> {
			builder.addLayer(0);
			clonedNetwork.getLayers().getLast().setNeurons(layer.getNeurons().entrySet().stream().map(mapEntry -> {
				Neuron clonedNeuron = new Neuron();
				clonedNeuron.setWeights(mapEntry.getValue().getWeights());
				clonedNeuron.setBias(mapEntry.getValue().getBias());
				return clonedNeuron;
			}).collect(Collectors.toMap(n -> IdUtil.getNewId(), n -> n)));

		});

		return clonedNetwork;
	}

}
