package neuralnetorks.exception;

public class ModelException extends RuntimeException {
	
	private static final long serialVersionUID = -6367600879627878602L;
	
	String msg;
	
	public ModelException(String message) {
		this.msg = message;
	}
	
	@Override
	public String getMessage() {
		return this.msg;
	}
	

}
