package neuralnetorks.utils;

public class IdUtil {
	
	private static Long nextId = 0L;
	
	public static Long getNewId() {
		nextId++;
		return nextId;
	}

	
}
