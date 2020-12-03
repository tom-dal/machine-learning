package neuralnetorks.utils;

public class IdUtil {
	
	private static Long nextId = 0L;
	private static Long DeepLayerCount = 0L;
	
	public static Long getNewId() {
		nextId++;
		return nextId;
	}

	public static Long getNextDeepLayerNumber() {
		DeepLayerCount++;
		return DeepLayerCount;
	}

	
}
