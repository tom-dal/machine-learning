package neuralnetorks.utils;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;

import neuralnetorks.model.Network;

public class MathUtilities {

	public static Random random = new Random();

	private static int staticIndex;

	public static double getRandomDouble() {
		double randomdouble;
		do {
			randomdouble = random.nextDouble();
		} while (randomdouble == 0);
		return randomdouble;
	}

//	public static double meanSquaredError(double[] predicted, double[] actual) {
//		double mse = 0;
//		for (int i = 0; i < actual.length; i++) {
//			mse += square(predicted[i] - actual[i]);
//		}
//		mse /= actual.length;
//		return mse;
//	}

	public static double square(double num) {
		return num * num;
	}

	public static double[][] copy2dArray(double[][] array) {
		return Arrays.stream(array).map(double[]::clone).toArray(double[][]::new);
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
			values[i] = rndGen.nextGaussian() * deviation + mean;
		}

		return values;

	}

	public double standardDeviation(double[] values) {
		double mean = Arrays.stream(values).sum() / values.length;
		return Math.sqrt(Arrays.stream(values).map(value -> MathUtilities.square(mean - value)).sum() / values.length);

	}

	public double getMseWeightSlope(Network network, double[][] inputBatch, double[] outputBatch,
			double[] targetBatch) {
		double sum = 0;
		for (int i = 0; i < targetBatch.length; i++) {
			sum += ((outputBatch[i] - targetBatch[i]) * inputBatch[i][0]);
		}
		return sum / targetBatch.length;
	}

	public double getMseBiasSlope(Network network, double[] outputBatch, double[] targetBatch) {
		double sum = 0;
		for (int i = 0; i < targetBatch.length; i++) {
			sum += (outputBatch[i] - targetBatch[i]);
		}
		return sum / targetBatch.length;
	}

	public static double[] normalizeArray(double[] targetData) {
		double min = Arrays.stream(targetData).min().getAsDouble();
		double span = Arrays.stream(targetData).max().getAsDouble() - min;
		for (int j = 0; j < targetData.length; j++) {
			double normValue = (targetData[j] - min) / span;
			targetData[j] = normValue;
		}
		return targetData;
	}

	public static void normalizeBatch(double[][] batch, double[] span, double[] min) {
		for (int i = 0; i < batch[0].length; i++) {
			staticIndex = i;
			Arrays.stream(batch)
					.forEach(array -> array[staticIndex] = (array[staticIndex] - min[staticIndex]) / span[staticIndex]);
		}
	}

	public static void normalize(double[][] batch) {
		double min[] = getMin(batch);
		double span[] = getSpan(batch, min);
		normalizeBatch(batch, span, min);
	}

	private static double[] getSpan(double[][] batch, double[] min) {
		if (min == null)
			min = getMin(batch);
		double[] span = new double[batch[0].length];
		for (int i = 0; i < batch[0].length; i++) {
			staticIndex = i;
			span[staticIndex] = Arrays.stream(batch).mapToDouble(array -> array[staticIndex]).max().getAsDouble()
					- min[staticIndex];
		}
		return span;
	}

	private static double[] getMin(double[][] batch) {
		double[] min = new double[batch[0].length];
		for (int i = 0; i < batch[0].length; i++) {
			staticIndex = i;
			min[staticIndex] = Arrays.stream(batch).mapToDouble(array -> array[staticIndex]).min().getAsDouble();
		}
		return min;
	}

	public static double absoluteError(double[] predicted, double[] actual) {
		double mae = 0;
		for (int i = 0; i < actual.length; i++) {
			mae += Math.abs(predicted[i] - actual[i]);
		}
		mae /= actual.length;
		return mae;
	}

	public static void center(double[][] batch, double[] inputAverage) {
		for (int i = 0; i < batch[0].length; i++) {
			staticIndex = i;
			Arrays.stream(batch).forEach(array -> array[staticIndex] -= inputAverage[staticIndex]);
		}
	}

	public static double[] average(double[][] batch) {
		double[] average = new double[batch[0].length];
		for (int i = 0; i < batch[0].length; i++) {
			staticIndex = i;
			average[staticIndex] = Arrays.stream(batch).mapToDouble(array -> array[staticIndex]).average()
					.getAsDouble();
		}
		return average;
	}

	public static double meanSquaredError(double[][] outputData, double[][] targetData) {
		double[][] outputBatch = copy2dArray(outputData);
		double[][] targetBatch = copy2dArray(targetData);
		double[] error = new double[targetData[0].length];
		for (int i = 0; i < error.length; i++) {
			error[i] = 0;
			for (int j = 0; j < targetData.length; j++) {
				error[i] += square(outputBatch[j][i] - targetBatch[j][i]);
			}
			error[i] /= targetData.length;
		}
		if (error.length>1) {
			error = normalizeArray(error);
		}
		return Arrays.stream(error).sum();
	}

	public static void centerSingleInput(double[] input, double[] inputAverage) {
		for (int i = 0; i < inputAverage.length; i++) {
			input[i] -= inputAverage[i];
		}
	}

	public static void normalizeSingleInput(double[] input, double[] inputMin, double[] inputSpan) {
		for (int i = 0; i < inputSpan.length; i++) {
			input[i] = (input[i] - inputMin[i])/inputSpan[i];
		}
	}

	public static void revertNormalization(double[] output, double[] inputMin, double[] inputSpan) {
		// TODO Auto-generated method stub
		
	}

	public static void revertCentering(double[] output, double[] inputAverage) {
		// TODO Auto-generated method stub
		
	}

}
