package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class represents an Problem instance of the SASTProblem
 */
public class SASTProblem {

	private double maxtime = 0.0;
	private double initstamina = 0.0;
	private double maxstamina = 0.0;
	private double alpha = 0.0;
	private double habitus = 0.0;
	private double speed = 0.0;
	private double startX = 0.0;
	private double startY = 0.0;
	private double numspot = 0.0;
	private double nummeth = 0.0;
	private HashMap<Double, Spot> spots = new HashMap<Double, Spot>();

	public double getMaxtime() {
		return maxtime;
	}

	public double getInitstamina() {
		return initstamina;
	}

	public double getMaxstamina() {
		return maxstamina;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getHabitus() {
		return habitus;
	}

	public double getSpeed() {
		return speed;
	}

	public double getStartX() {
		return startX;
	}

	public double getStartY() {
		return startY;
	}

	/**
	 * Standard constructor, which creates an representation of an SASTP out of
	 * an problem instance file.
	 * 
	 * @param inputLocation
	 *            a String path to the location of the problem instance file
	 */
	public SASTProblem(String inputLocation) throws IOException,
			NumberFormatException, InvalidInputException {
		FileReader fr = new FileReader(inputLocation);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		int counter = 1;
		boolean invalidInput = false;
		Spot currentSpot = null;
		while ((line = br.readLine()) != null && !invalidInput) {
			// System.out.println(line);
			String[] splittedInput = line.split("\\s+");
			if (splittedInput.length == 2) {
				if (counter == 1 && splittedInput[0].equals("maxtime")) {
					maxtime = Double.parseDouble(splittedInput[1]);
				} else if (counter == 2 && splittedInput[0].equals("initst")) {
					initstamina = Double.parseDouble(splittedInput[1]);
				} else if (counter == 3 && splittedInput[0].equals("maxst")) {
					maxstamina = Double.parseDouble(splittedInput[1]);
				} else if (counter == 4 && splittedInput[0].equals("alpha")) {
					alpha = Double.parseDouble(splittedInput[1]);
				} else if (counter == 5 && splittedInput[0].equals("habitus")) {
					habitus = Double.parseDouble(splittedInput[1]);
				} else if (counter == 6 && splittedInput[0].equals("speed")) {
					speed = Double.parseDouble(splittedInput[1]);
				} else if (counter == 8 && splittedInput[0].equals("numspot")) {
					numspot = Double.parseDouble(splittedInput[1]);
				} else if (counter == 9 && splittedInput[0].equals("nummeth")) {
					nummeth = Double.parseDouble(splittedInput[1]);
				} else {
					invalidInput = true;
				}
			} else if (splittedInput.length == 3) {
				if (counter == 7 && splittedInput[0].equals("start")) {
					startX = Double.parseDouble(splittedInput[1]);
					startY = Double.parseDouble(splittedInput[2]);
				} else {
					invalidInput = true;
				}
			} else if (splittedInput.length == 5 && counter > 9
					&& counter < (10 + numspot + nummeth)) {
				if (splittedInput[0].equals("spot")) {
					double sID = Double.parseDouble(splittedInput[1]);
					currentSpot = new Spot(sID, splittedInput[2],
							Double.parseDouble(splittedInput[3]),
							Double.parseDouble(splittedInput[4]));
					spots.put(sID, currentSpot);
				} else if (splittedInput[0].equals("method")) {
					if (currentSpot != null) {
						currentSpot.addMethod(splittedInput[1],
								Double.parseDouble(splittedInput[2]),
								Double.parseDouble(splittedInput[3]),
								Double.parseDouble(splittedInput[4]));
					} else {
						invalidInput = true;
					}
				} else {
					invalidInput = true;
				}
			}
			counter++;
		}

		if (invalidInput) {
			throw new InvalidInputException();
		}
	}

	/**
	 * Returns the distance between two pairs(x1,y1)(x2,y2) of coordinates
	 * 
	 * @param x1
	 *            a double, representing the x coordinate of the first pair
	 * @param y1
	 *            a double, representing the y coordinate of the first pair
	 * @param x2
	 *            a double, representing the x coordinate of the second pair
	 * @param y2
	 *            a double, representing the y coordinate of the second pair
	 * 
	 * @return double, distance between two Spots
	 */
	public double getDistance(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt((dx * dx) + (dy * dy));
	}

	/**
	 * Returns the distance between two Spots
	 * 
	 * @param A
	 *            a Spot, representing the start
	 * @param B
	 *            a Spot, representing the end
	 * 
	 * @return double, distance between two Spots
	 * @see Spot
	 */
	public double getDistance(Spot A, Spot B) {
		return getDistance(A.getSpotX(), A.getSpotY(), B.getSpotX(),
				B.getSpotY());
	}

	/**
	 * Returns the satisfaction cost of a travel between two Spo
	 * 
	 * @param x1
	 *            a double, representing the x coordinate of the first pair
	 * @param y1
	 *            a double, representing the y coordinate of the first pair
	 * @param x2
	 *            a double, representing the x coordinate of the second pair
	 * @param y2
	 *            a double, representing the y coordinate of the second pair
	 * 
	 * @return double, travel satisfaction between two Spots
	 */
	public double getTravelSatisfactionCost(double x1, double y1, double x2, double y2) {
		return alpha * getDistance(x1, y1, x2, y2);
	}
	
	/**
	 * Returns the satisfaction cost of a travel between two Spots
	 * 
	 * @param A
	 *            a Spot, representing the start
	 * @param B
	 *            a Spot, representing the end
	 * 
	 * @return double, travel satisfaction between two Spots
	 * @see Spot
	 */
	public double getTravelSatisfactionCost(Spot A, Spot B) {
		return alpha * getDistance(A, B);
	}

	/**
	 * Returns the travel time between two Spots
	 * 
	 * @param x1
	 *            a double, representing the x coordinate of the first pair
	 * @param y1
	 *            a double, representing the y coordinate of the first pair
	 * @param x2
	 *            a double, representing the x coordinate of the second pair
	 * @param y2
	 *            a double, representing the y coordinate of the second pair
	 * 
	 * @return double, travel time between two Spots
	 */
	public double getTravelTime(double x1, double y1, double x2, double y2) {
		return getDistance(x1, y1, x2, y2) / speed;
	}
	
	/**
	 * Returns the travel time between two Spots
	 * 
	 * @param A
	 *            a Spot, representing the start
	 * @param B
	 *            a Spot, representing the end
	 * 
	 * @return double, travel time between two Spots
	 * @see Spot
	 */
	public double getTravelTime(Spot A, Spot B) {
		return getDistance(A, B) / speed;
	}

	/**
	 * Returns the spots of a problem instance.
	 * 
	 * @return HashMap<Double, Spot>, all spots that belong to the problem
	 *         instance
	 */
	public HashMap<Double, Spot> getSpots() {
		return spots;
	}

	@Override
	public String toString() {
		String retString = "";
		for (double key : spots.keySet()) {
			retString = retString + spots.get(key).toString();
		}
		return retString;
	}
}
