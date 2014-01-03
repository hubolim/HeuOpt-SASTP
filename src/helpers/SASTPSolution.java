package helpers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents an solution for a SASTProblem.
 */
public class SASTPSolution {
	private SASTProblem problem;
	private ArrayList<Stop> tour = new ArrayList<Stop>();
	private int iterator;

	/**
	 * Standard constructor
	 * 
	 * @param problem
	 *            a SASTProblem problem instance
	 * @see SASTProblem
	 */
	public SASTPSolution(SASTProblem problem) {
		this.problem = problem;
		iterator = 0;
	}

	public int getTourSize() {
		return tour.size();
	}

	public void setNextStop() {
		iterator++;
		if (iterator >= tour.size()) {
			iterator = 0;
		}
	}

	public void setNextStop(int iter) {
		if (iter < tour.size()) {
			iterator = iter;
		} else {
			iterator = 0;
		}
	}

	public Spot getSpot() {
		return tour.get(iterator).getSpot();
	}

	public Method getMethod() {
		return tour.get(iterator).getMethod();
	}

	public double getRestingTime() {
		return tour.get(iterator).getRestingTime();
	}

	/**
	 * Adds a stop to the tour
	 * 
	 * @param spot
	 *            the current Spot used
	 * @param method
	 *            the current Method used
	 * @param restingTime
	 *            double, the time rested
	 * 
	 * @see Spot
	 * @see Method
	 */
	public void addStop(Spot spot, Method method, double restingTime) {
		tour.add(new Stop(spot, method, restingTime));
	}

	/**
	 * Adds a rest to the last stop of the tour
	 * 
	 * @param restingTime
	 *            double, the time rested
	 */
	public void addRest(double restingTime) {
		tour.get(tour.size() - 1).setRestingTime(restingTime);
	}

	/**
	 * Returns the time that is left of the tour
	 * 
	 * @return double the time left of the current tour
	 */
	public double getTimeLeft() {
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();

		double methodTime = 0.0;
		double restTime = 0.0;
		double travelTime = 0.0;
		for (int i = 0; i < tour.size(); i++) {
			methodTime = methodTime + tour.get(i).getMethod().getTime();
			restTime = restTime + tour.get(i).getRestingTime();
			travelTime = travelTime
					+ problem.getTravelTime(currentX, currentY, tour.get(i)
							.getSpot().getSpotX(), tour.get(i).getSpot()
							.getSpotY());
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		return problem.getMaxtime() - methodTime - restTime - travelTime;
	}

	/**
	 * Returns the final time that is left of the finished tour.
	 * 
	 * @return double the final time left of the finished tour
	 */
	public double getFinalTimeLeft() {
		return getTimeLeft()
				- problem.getTravelTime(tour.get(tour.size() - 1).getSpot()
						.getSpotX(), tour.get(tour.size() - 1).getSpot()
						.getSpotY(), problem.getStartX(), problem.getStartY());

	}

	/**
	 * Returns the stamina left of the tour
	 * 
	 * @return double the stamina left of the current tour
	 */
	public double getStaminaLeft() {
		double staminaLeft = problem.getInitstamina();
		for (int i = 0; i < tour.size(); i++) {
			staminaLeft = staminaLeft
					+ (tour.get(i).getRestingTime() * problem.getHabitus());
			staminaLeft = staminaLeft - tour.get(i).getMethod().getStamina();
		}
		return staminaLeft;
	}

	/**
	 * Returns the current satisfaction of the tour
	 * 
	 * @return double the satisfaction of the current tour
	 */
	public double getSatisfaction() {
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();
		double satisfaction = 0.0;
		for (int i = 0; i < tour.size(); i++) {
			double travelSatisfaction = problem.getTravelSatisfactionCost(
					currentX, currentY, tour.get(i).getSpot().getSpotX(), tour
							.get(i).getSpot().getSpotY());
			satisfaction = satisfaction
					+ tour.get(i).getMethod().getSatisfaction();
			satisfaction = satisfaction - travelSatisfaction;
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		return satisfaction;
	}

	/**
	 * Returns the final satisfaction of the finished tour.
	 * 
	 * @return double the final satisfaction of the finished tour
	 */
	public double getFinalSatisfaction() {
		return getSatisfaction()
				- problem.getTravelSatisfactionCost(tour.get(tour.size() - 1)
						.getSpot().getSpotX(), tour.get(tour.size() - 1)
						.getSpot().getSpotY(), problem.getStartX(),
						problem.getStartY());

	}

	/**
	 * Returns a boolean whether the Spot was already visited
	 * 
	 * @param spot
	 *            the spot that is checked whether it was already visited
	 * 
	 * @return boolean whether the Spot was already visited
	 */
	public boolean isSpotAlreadyVisited(Spot spot) {
		for (int i = 0; i < tour.size(); i++) {
			if (tour.get(i).getSpot().equals(spot)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a boolean whether the Spot�s Method was already visited
	 * 
	 * @param spot
	 *            the spot that is checked whether it was already visited
	 * @param method
	 *            the method that is checked whether it was already visited
	 * 
	 * @return boolean whether the Spot�s Method was already visited
	 */
	public boolean isSpotMethodAlreadyVisited(Spot spot, Method method) {
		for (int i = 0; i < tour.size(); i++) {
			if (tour.get(i).getSpot().equals(spot)
					&& tour.get(i).getMethod().equals(method)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the solution is valid.
	 * 
	 * @return boolean, true if the solution is valid
	 */
	public boolean checkSolution() {
		double startStamina = problem.getInitstamina();
		double maxStamina = problem.getMaxstamina();
		double maxTime = problem.getMaxtime();
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();
		double alpha = problem.getAlpha();
		double habitus = problem.getHabitus();
		double speed = problem.getSpeed();
		double currentStamina = startStamina;
		double currentTime = maxTime;
		double currentSatisfaction = 0.0;
		ArrayList<Spot> visitedSPots = new ArrayList<Spot>();
		for (int i = 0; i < tour.size(); i++) {
			Spot spot = tour.get(i).getSpot();
			if (visitedSPots.contains(spot)) {
				return false;
			} else {
				visitedSPots.add(spot);
			}
			double distance = problem.getDistance(currentX, currentY,
					spot.getSpotX(), spot.getSpotY());
			currentTime = currentTime - (distance / speed)
					- tour.get(i).getRestingTime()
					- tour.get(i).getMethod().getTime();
			currentStamina = currentStamina
					- tour.get(i).getMethod().getStamina()
					+ (tour.get(i).getRestingTime() * habitus);
			currentSatisfaction = currentSatisfaction
					+ tour.get(i).getMethod().getSatisfaction()
					- (distance * alpha);
			if ((currentStamina - (tour.get(i).getRestingTime() * habitus) < 0.0)
					|| (currentTime < 0.0) || (currentStamina > maxStamina)) {
				return false;
			}
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		double distance = problem.getDistance(currentX, currentY,
				problem.getStartX(), problem.getStartY());
		currentTime = currentTime - (distance / speed);
		currentSatisfaction = currentSatisfaction - (distance * alpha);
		// if ((currentStamina < 0.0) || (currentTime < 0.0)
		// || (currentStamina > maxStamina)
		// || (roundTwoDecimals(getFinalTimeLeft()) !=
		// roundTwoDecimals(currentTime))
		// || (roundTwoDecimals(getFinalSatisfaction()) !=
		// roundTwoDecimals(currentSatisfaction))
		// || (roundTwoDecimals(getStaminaLeft()) !=
		// roundTwoDecimals(currentStamina))) {
		// return false;
		// }
		if ((currentStamina < 0.0) || (currentTime < 0.0)
				|| (currentStamina > maxStamina) || getFinalTimeLeft() < 0.0
				|| getStaminaLeft() < 0.0 || getStaminaLeft() > maxStamina) {
			return false;
		}
		return true;
	}

	private double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d).replace(',', '.'));
	}

	/**
	 * Returns all unused Spots of the solution.
	 * 
	 * @return ArrayList<Spot>, of all unused Spots
	 */
	public ArrayList<Spot> getUnusedSpots() {
		ArrayList<Spot> unusedSpots = new ArrayList<Spot>();
		ArrayList<Spot> spots = new ArrayList<Spot>(problem.getSpots().values());
		int iter = 0;
		while (iter < tour.size()) {
			if (!spots.contains(tour.get(iter).getSpot())) {
				unusedSpots.add(tour.get(iter).getSpot());
			}
			iter++;
		}
		return unusedSpots;
	}

	public void saveSolution(String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.println("#Solution to SASTP");
			writer.println("#Spot Method Resting Time");
			for (int i = 0; i < tour.size(); i++) {
				writer.println((int) tour.get(i).getSpot().getID() + " "
						+ (int) tour.get(i).getMethod().getID() + " "
						+ tour.get(i).getRestingTime());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * public void checkSolution() { double startStamina =
	 * problem.getInitstamina(); double maxStamina = problem.getMaxstamina();
	 * double maxTime = problem.getMaxtime(); double currentX =
	 * problem.getStartX(); double currentY = problem.getStartY(); double alpha
	 * = problem.getAlpha(); double habitus = problem.getHabitus(); double speed
	 * = problem.getSpeed(); double currentStamina = startStamina; double
	 * currentTime = maxTime; double currentSatisfaction = 0.0;
	 * 
	 * System.out.println("Start Stamina: " + currentStamina + ", Start Time: "
	 * + currentTime + ", Max Stamina: " + maxStamina); for (int i = 0; i <
	 * tour.size(); i++) { Spot spot = tour.get(i).getSpot(); Method method =
	 * tour.get(i).getMethod(); double distance = problem.getDistance(currentX,
	 * currentY, spot.getSpotX(), spot.getSpotY()); currentTime = currentTime -
	 * (distance / speed) - tour.get(i).getRestingTime() -
	 * tour.get(i).getMethod().getTime(); currentStamina = currentStamina -
	 * tour.get(i).getMethod().getStamina() + (tour.get(i).getRestingTime() *
	 * habitus); currentSatisfaction = currentSatisfaction +
	 * tour.get(i).getMethod().getSatisfaction() - (distance * alpha);
	 * System.out .println("After stop with rest " + i + ": StaminaBefore " +
	 * (currentStamina - (tour.get(i).getRestingTime() * habitus)) +
	 * ", Stamina " + currentStamina + ", Time " + currentTime +
	 * " Satisfaction: " + currentSatisfaction); currentX =
	 * tour.get(i).getSpot().getSpotX(); currentY =
	 * tour.get(i).getSpot().getSpotY(); } double distance =
	 * problem.getDistance(currentX, currentY, problem.getStartX(),
	 * problem.getStartY()); currentTime = currentTime - (distance / speed);
	 * System.out.println("End Stamina: " + currentStamina + ", End Time: " +
	 * currentTime); }
	 */
}
