package neuralnetorks.utils;

public class HashUtil {
	
	private static Long hashCount = 0L;
	
	public static Long getNewHashCode() {
		hashCount++;
		return hashCount;
	}

	
}
