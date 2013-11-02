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

	@Override
	public String toString() {
		String retString = "";
		for (double key : spots.keySet()) {
			retString = retString + spots.get(key).toString();
		}
		return retString;
	}
}
