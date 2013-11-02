package helpers;

/**
 * Representing a Method of the Stamina Aware Sightseeing Tour Problem.
 */
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

	/**
	 * Standard constructor
	 * 
	 * @param ID
	 *            a double, representing the ID of the method
	 * @param name
	 *            a string, representing the name of the method
	 * @param satisfaction
	 *            a double, representing the satisfaction of the method
	 * @param time
	 *            a double, representing the time of the method
	 * @param stamina
	 *            a double, representing the stamina of the method
	 */
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
