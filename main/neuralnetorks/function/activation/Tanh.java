package neuralnetorks.function.activation;

import neuralnetorks.function.interfaces.ActivationFunctionInterface;

public class Tanh implements ActivationFunctionInterface {
	
	private static Tanh istance;

	@Override
	public double apply(double output) {
		return (Math.exp(2*output)-1)/(Math.exp(2*output)+1);
	}
	
	private Tanh() {}
	
	public static Tanh getIstance() {
		if (istance == null) istance = new Tanh();
		return istance;
	}

}
