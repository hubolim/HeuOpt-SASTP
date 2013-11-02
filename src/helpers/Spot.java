package helpers;

import java.util.HashMap;

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

	public Spot(double ID, String name, double spotX, double spotY) {
		this.ID = ID;
		this.name = name;
		this.spotX = spotX;
		this.spotY = spotY;
	}

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
