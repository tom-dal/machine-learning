package neuralnetorks.function.activation;

import neuralnetorks.function.interfaces.ActivationFunctionInterface;
import neuralnetorks.config.Configuration;

public class ELU implements ActivationFunctionInterface {
	
	private static ELU istance;

	@Override
	public double apply(double output) {
		if (output>0) return output;
		else return Configuration.ELU_CONSTANT*(Math.exp(output)-1);	
	}
	
	private ELU() {}
	
	public static ELU getIstance() {
		if (istance==null) istance = new ELU();
		return istance;
	}

}
