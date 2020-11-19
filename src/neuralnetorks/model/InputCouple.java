package neuralnetorks.model;

public class InputCouple {
	
	private double input;
	private double weight;
	
	public InputCouple(double input, double weight) {
		super();
		this.input = input;
		this.weight = weight;
	}
	
	public double getInput() {
		return input;
	}
	
	public void setInput(double input) {
		this.input = input;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public double multiplyInputWeight() {
		return input*weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(input);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputCouple other = (InputCouple) obj;
		if (Double.doubleToLongBits(input) != Double.doubleToLongBits(other.input))
			return false;
		if (Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight))
			return false;
		return true;
	}
	
	

}
