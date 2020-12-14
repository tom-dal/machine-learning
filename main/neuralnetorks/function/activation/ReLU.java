package neuralnetorks.function.activation;

import neuralnetorks.function.interfaces.ActivationFunctionInterface;

public class ReLU implements ActivationFunctionInterface {
	
	private static ReLU istance;

	@Override
	public double apply(double output) {
		return Math.max(0, output);
	}
	
	private ReLU() {}
	
	public static ReLU getIstance() {
		if (istance==null) istance = new ReLU();
		return istance;
	}

}
