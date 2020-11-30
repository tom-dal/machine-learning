package neuralnetorks.exception;

@SuppressWarnings("serial")
public class InputCompilingException extends RuntimeException {
	
	private String msg;
	
	public InputCompilingException() {
		
	}
	
	public InputCompilingException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return this.msg;
	}
	
	

}
