package neuralnetorks.function.interfaces;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.function.activation.Identity;
import neuralnetorks.function.activation.ReLU;
import neuralnetorks.function.activation.Sigmoid;
import neuralnetorks.function.activation.Tanh;

public interface ActivationFunctionInterface {
	
	public double apply(double output);
	
	public static ActivationFunctionInterface getInstance(ActivationFunctions activationFunction) {
		if(activationFunction.equals(ActivationFunctions.SIGMOID)) return Sigmoid.getIstance();
		if(activationFunction.equals(ActivationFunctions.RELU)) return ReLU.getIstance();
		if(activationFunction.equals(ActivationFunctions.TANH)) return Tanh.getIstance();
		else return Identity.getIstance();
	}
}
