package helpers;

public class Method {
	private double ID;
	private String name;
	private double satisfaction;
	private double time;
	private double stamina;

	public double getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public double getSatisfaction() {
		return satisfaction;
	}

	public double getTime() {
		return time;
	}

	public double getStamina() {
		return stamina;
	}

	public Method(double ID, String name, double satisfaction, double time,
			double stamina) {
		this.ID = ID;
		this.name = name;
		this.satisfaction = satisfaction;
		this.time = time;
		this.stamina = stamina;
	}

	@Override
	public String toString() {
		return "Method " + ID + ": " + name + ", Satisfaction: " + satisfaction
				+ ", Time: " + time + ", Stamina: " + stamina;
	}
}
