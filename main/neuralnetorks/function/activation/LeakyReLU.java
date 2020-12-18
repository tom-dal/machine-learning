package neuralnetorks.function.activation;

import neuralnetorks.config.Configuration;
import neuralnetorks.function.interfaces.ActivationFunctionInterface;

public class LeakyReLU implements ActivationFunctionInterface {
	
	private static LeakyReLU istance;

	@Override
	public double apply(double output) {
		if (output==0) return 0;
		else if(output<0) return Configuration.LReLU_CONSTANT * output;
		else return output;	
	}
	
	private LeakyReLU() {}
	
	public static LeakyReLU getIstance() {
		if (istance==null) istance = new LeakyReLU();
		return istance;
	}

}
