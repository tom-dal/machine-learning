package neuralnetorks.function.interfaces;

import neuralnetorks.enums.ActivationFunctions;
import neuralnetorks.function.activation.ELU;
import neuralnetorks.function.activation.Identity;
import neuralnetorks.function.activation.LeakyReLU;
import neuralnetorks.function.activation.ReLU;
import neuralnetorks.function.activation.Sigmoid;
import neuralnetorks.function.activation.Tanh;

public interface ActivationFunctionInterface {
	
	public double apply(double output);
	
	public static ActivationFunctionInterface getInstance(ActivationFunctions activationFunction) {
		if(activationFunction.equals(ActivationFunctions.SIGMOID)) return Sigmoid.getIstance();
		else if(activationFunction.equals(ActivationFunctions.ReLU)) return ReLU.getIstance();
		else if(activationFunction.equals(ActivationFunctions.TANH)) return Tanh.getIstance();
		else if(activationFunction.equals(ActivationFunctions.LReLU)) return LeakyReLU.getIstance();
		if(activationFunction.equals(ActivationFunctions.ELU)) return ELU.getIstance();
		else return Identity.getIstance();
	}
}
