package neuralnetorks.exception;

import java.util.Set;

import neuralnetorks.checklists.PreInitializationCheckList;

public class ModelBuildingException extends RuntimeException {
	
	private static final long serialVersionUID = -6367600879627878602L;
	
	String msg;
	
	public ModelBuildingException(Set<PreInitializationCheckList> misses) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThe following errors have been encountered while initializing the network: \n");
		misses.forEach(error -> sb.append(error.getErrorMessage() + "\n"));
		
		this.msg = sb.toString();
	}
	
	@Override
	public String getMessage() {
		return this.msg;
	}
	

}
