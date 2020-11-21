package neuralnetorks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import neuralnetorks.builder.NetworkBuilder;
import neuralnetorks.function.MeanSquaredError;
import neuralnetorks.model.Layer;
import neuralnetorks.model.Network;
import neuralnetorks.model.Neuron;
import neuralnetorks.utils.HashUtil;
import neuralnetorks.utils.MathUtils;

public class LearningCore {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private double learningRate;
	private MeanSquaredError errorFunction;
	private Map<Integer, Neuron> neuronMap;
	
	public enum ErrorFunctions{
		MEAN_SQUARED_ERROR;
	}

	public void setLearningRate(double lr) {
		this.learningRate=lr;
	}

	public void setErrorFunction(ErrorFunctions function) {
		if (function.equals(ErrorFunctions.MEAN_SQUARED_ERROR)) {
			this.errorFunction=new MeanSquaredError();
		}	
	}

	public void teach(Network fcn, double[][] inputData, double[] targetData, int iterations) {
		
		for (int i = 0; i < iterations; i++) {
			double[][] predicted = processNetwork(fcn, inputData, predicted);
			double mse = this.errorFunction.getError(predicted , targetData);
		}
		
	}

	private double[][] processNetwork(Network network, double[][] inputData) {
		double[][] predicted = new double[inputData.length][];
		for (int i = 0; i < inputData.length; i++) {
			double[] input = inputData[i];
			double[] output = null;
			for (Layer layer : network.getLayers()) {
				output = layer.process(input);
			}
			if(output.length==1) {
				predicted[i] = output;
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
		network.getLayers().forEach(layer ->{
			builder.addDenseLayer(0);
			clonedNetwork.getLayers().getLast().setNeurons(layer.getNeurons().entrySet().stream().map( mapEntry -> {
				Neuron clonedNeuron = new Neuron();
				clonedNeuron.setWeights(mapEntry.getValue().getWeights());
				clonedNeuron.setBias(mapEntry.getValue().getBias());
				return clonedNeuron;
			}).collect(Collectors.toMap(n -> HashUtil.getNewHashCode(), n -> n)));
			
		});
		
		return clonedNetwork;
	}
	

}
