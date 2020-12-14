package neuralnetorks.function.activation;

import neuralnetorks.function.interfaces.ActivationFunctionInterface;

public class Identity implements ActivationFunctionInterface {
	
	private static Identity istance;

	@Override
	public double apply(double output) {
		return output;
	}
	
	private Identity() {}
	
	public static Identity getIstance() {
		if (istance==null) istance = new Identity();
		return istance;
	}
	

}
