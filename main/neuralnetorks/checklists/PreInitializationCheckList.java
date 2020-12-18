package neuralnetorks.checklists;

public enum PreInitializationCheckList  {
	
	INPUTLAYER_ADDED,
	OUTPUTLAYER_ADDED;
	
	public String getErrorMessage() {
		if (this.equals(PreInitializationCheckList.INPUTLAYER_ADDED)) {
			return "Input layer is missing";
		} 
		else if (this.equals(PreInitializationCheckList.OUTPUTLAYER_ADDED)) {
			return "Output layer is missing";
		}
		return null;
	}

}
