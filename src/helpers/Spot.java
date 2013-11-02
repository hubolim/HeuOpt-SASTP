package helpers;

import java.util.HashMap;

/**
 * Representing a Spot of the Stamina Aware Sightseeing Tour Problem.
 */
public class Spot {
	private double ID;
	private String name;
	private double spotX, spotY;
	private HashMap<Double, Method> methods = new HashMap<Double, Method>();

	public double getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public double getSpotX() {
		return spotX;
	}

	public double getSpotY() {
		return spotY;
	}

	/**
	 * Standard constructor
	 * 
	 * @param ID
	 *            a double, representing the ID of the spot
	 * @param name
	 *            a string, representing the name of the spot
	 * @param spotX
	 *            a double, representing the x coordinate on a 2D map
	 * @param spotY
	 *            a double, representing the y coordinate on a 2D m
	 */
	public Spot(double ID, String name, double spotX, double spotY) {
		this.ID = ID;
		this.name = name;
		this.spotX = spotX;
		this.spotY = spotY;
	}

	/**
	 * Method used to add a Method to a spot
	 * 
	 * @param name
	 *            a string, representing the name of the method
	 * @param satisfaction
	 *            a double, representing the satisfaction of the method
	 * @param time
	 *            a double, representing the time of the method
	 * @param stamina
	 *            a double, representing the stamina of the method
	 * 
	 * @see Method
	 */
	public void addMethod(String name, double satisfaction, double time,
			double stamina) {
		String[] splittedName = name.split(":");
		double mID = Double.parseDouble(splittedName[1]);
		Method newMethod = new Method(mID, name, satisfaction, time, stamina);
		methods.put(mID, newMethod);
	}

	@Override
	public String toString() {
		String retString = "Spot " + ID + ": " + name + ", SpotX: " + spotX
				+ ", SpotY: " + spotY + System.getProperty("line.separator");
		for (double key : methods.keySet()) {
			retString = retString + "  " + methods.get(key).toString()
					+ System.getProperty("line.separator");
		}
		return retString;
	}
}
