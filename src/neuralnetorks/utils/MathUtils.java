package neuralnetorks.utils;

import java.util.Random;

public class MathUtils {
	
	public static Random random = new Random();
	
	public static double[] getWeights(int inputSize) {
		double[] weights = new double[inputSize];
		for (int i=0;i < weights.length; i++) {
			do {
				weights[i]=random.nextDouble();
			} while (weights[i]==0);
		}
		return weights;
	}

}
