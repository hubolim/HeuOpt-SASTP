package helpers;

import java.util.ArrayList;

/**
 * This class represents an solution for a SASTProblem.
 */
public class SASTPSolution {
	private SASTProblem problem;
	private ArrayList<Stop> tour = new ArrayList<Stop>();

	/**
	 * Standard constructor
	 * 
	 * @param problem
	 *            a SASTProblem problem instance
	 * @see SASTProblem
	 */
	public SASTPSolution(SASTProblem problem) {
		this.problem = problem;
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
					+ (problem.getDistance(currentX, currentY, tour.get(i)
							.getSpot().getSpotX(), tour.get(i).getSpot()
							.getSpotY()) / problem.getSpeed());
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		return problem.getMaxtime() - methodTime - restTime - travelTime;
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
	 * Returns the current statisfaction of the tour
	 * 
	 * @return double the satisfaction of the current tour
	 */
	public double getSatisfaction() {
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();
		double satisfaction = 0.0;
		for (int i = 0; i < tour.size(); i++) {
			double travelDistance = problem.getDistance(currentX, currentY,
					tour.get(i).getSpot().getSpotX(), tour.get(i).getSpot()
							.getSpotY());
			satisfaction = satisfaction
					+ tour.get(i).getMethod().getSatisfaction();
			satisfaction = satisfaction - (travelDistance * problem.getAlpha());
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		return satisfaction;
	}

	/**
	 * Returns a boolean whether the Spot was already visited
	 * 
	 * @param spot
	 *            the spot that is checked whther it was already visited
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

	public boolean isSpotMethodAlreadyVisited(Spot spot, Method method) {
		for (int i = 0; i < tour.size(); i++) {
			if (tour.get(i).getSpot().equals(spot)
					&& tour.get(i).getMethod().equals(method)) {
				return true;
			}
		}
		return false;
	}

	public void checkSolution() {
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

		System.out.println("Start Stamina: " + currentStamina
				+ ", Start Time: " + currentTime + ", Max Stamina: "
				+ maxStamina);
		for (int i = 0; i < tour.size(); i++) {
			Spot spot = tour.get(i).getSpot();
			Method method = tour.get(i).getMethod();
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
			System.out
					.println("After stop with rest "
							+ i
							+ ": StaminaBefore "
							+ (currentStamina - (tour.get(i).getRestingTime() * habitus))
							+ ", Stamina " + currentStamina + ", Time "
							+ currentTime + " Satisfaction: "
							+ currentSatisfaction);
			currentX = tour.get(i).getSpot().getSpotX();
			currentY = tour.get(i).getSpot().getSpotY();
		}
		double distance = problem.getDistance(currentX, currentY,
				problem.getStartX(), problem.getStartY());
		currentTime = currentTime - (distance / speed);
		System.out.println("End Stamina: " + currentStamina + ", End Time: "
				+ currentTime);
	}

	private class Stop {
		private Spot spot;
		private Method method;
		double restingTime;

		public Spot getSpot() {
			return spot;
		}

		public Method getMethod() {
			return method;
		}

		public double getRestingTime() {
			return restingTime;
		}

		public void setRestingTime(double restingTime) {
			this.restingTime = restingTime;
		}

		private Stop(Spot spot, Method method, double restingTime) {
			this.spot = spot;
			this.method = method;
			this.restingTime = restingTime;
		}
	}
}
