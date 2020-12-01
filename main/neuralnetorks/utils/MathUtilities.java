package neuralnetorks.utils;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;

import neuralnetorks.model.Network;

public class MathUtilities {
	
	public static Random random = new Random();
	
	public static double getRandomDouble() {
		double randomdouble;
		do {
			randomdouble = random.nextDouble();
		} while (randomdouble==0);
		return randomdouble;
	}
	
	public static double meanSquaredError(double[] predicted, double[] actual) {
		double mse = 0;
		for (int i = 0; i < actual.length; i++) {
			mse += square(predicted[i] - actual[i]);
		}
		mse/=actual.length;
		return mse;
	}
	
	public static double square(double num) {
		return num*num;
	}
	
	public static double[] doubleToArray(double d) {
		double[] array = new double[1];
		array[0] = d;
		return array;
	}
	
	public static double arrayToDouble(double[] d) {
		return d[0];
	}
	
	public double[] getSndValues(int valuesNumber, double mean, double deviation) {
		RandomGenerator rndGen = RandomGeneratorFactory.createRandomGenerator(new Random());
		double[] values = new double[valuesNumber];
		for (int i = 0; i < values.length; i++) {
			values[i]=rndGen.nextGaussian()*deviation+mean;
		}
		
		return values;
		
	}

	public double standardDeviation(double[] values) {
		double mean = Arrays.stream(values).sum()/values.length;
		return Math.sqrt(Arrays.stream(values).map(value -> MathUtilities.square(mean-value)).sum()/values.length);
		
	}

	public double getMseWeightSlope(Network network, double[][] inputBatch, double[] outputBatch, double[] targetBatch) {
		double sum = 0;
		for (int i = 0; i < targetBatch.length; i++) {
			sum += ((outputBatch[i] - targetBatch[i]) * inputBatch[i][0]);
		}
		return sum/targetBatch.length;
	}
	
	public double getMseBiasSlope(Network network, double[] outputBatch, double[] targetBatch) {
		double sum = 0;
		for (int i = 0; i < targetBatch.length; i++) {
			sum += (outputBatch[i] - targetBatch[i]);
		}
		return sum/targetBatch.length;
	}

	public static double[][] normalize(double[][] inputDataArray) {
		double min = Arrays.stream(inputDataArray).mapToDouble(i -> i[0]).min().getAsDouble();
		double span = Arrays.stream(inputDataArray).mapToDouble(i -> i[0]).max().getAsDouble() - min;
		for (int j = 0; j < inputDataArray.length; j++) {
			double[] normValue = {(inputDataArray[j][0] - min)/span};
			inputDataArray[j] = normValue;
			
		}
		return inputDataArray;
	}

	public static double[] normalize(double[] targetData) {
		double min = Arrays.stream(targetData).min().getAsDouble();
		double span = Arrays.stream(targetData).max().getAsDouble() - min;
		for (int j = 0; j < targetData.length; j++) {
			double normValue = (targetData[j] - min)/span;
			targetData[j] = normValue;
			
		}
		return targetData;
	}

	public static double[] normalize(double[] input, double inputMin, double inputSpan) {
		input[0] = (input[0] - inputMin)/inputSpan;
		return input;
	}

}
