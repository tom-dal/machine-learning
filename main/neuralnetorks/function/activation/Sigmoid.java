package neuralnetorks.function.activation;

import neuralnetorks.function.interfaces.ActivationFunctionInterface;

public class Sigmoid implements ActivationFunctionInterface {
	
	private static Sigmoid istance;

	@Override
	public double apply(double output) {
		return 1/ (1 + Math.exp(-output));
	}

	private Sigmoid() {}
	
	public static Sigmoid getIstance() {
		if (istance==null) istance = new Sigmoid();
		return istance;
	}
}
