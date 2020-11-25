package neuralnetorks.utils;

import java.util.Random;

public class MathUtils {
	
	public static Random random = new Random();
	
	public static Double getRandomDouble() {
		return (Double) random.nextDouble();
	}
	
	public static double meanSquaredError(double[][] predicted, double[] actual) {
		double mse = 0;
		for (int i = 0; i < actual.length; i++) {
			mse += square(predicted[i][0] - actual[i]);
		}
		mse/=actual.length;
		return mse;
	}
	
	private static double square(double num) {
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

}
