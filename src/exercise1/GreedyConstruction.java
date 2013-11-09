package exercise1;

import java.util.ArrayList;
import java.util.HashMap;

import helpers.Method;
import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Spot;

/**
 * This class produces a SASTPSolution out of a SASTProblem using a greedy
 * heuristic approach.
 */
public class GreedyConstruction {
	private SASTProblem problem;
	private SASTPSolution solution;

	public SASTPSolution getSolution() {
		return solution;
	}

	/**
	 * Standard constructor, which creates a SASTPSolution out of a SASTProblem,
	 * using a greedy heuristic approach.
	 * 
	 * @param problem
	 *            a SASTProblem representing the problem instance
	 */
	public GreedyConstruction(SASTProblem problem) {
		this.problem = problem;

		// timeCounter can be printed for the user, to see that the system is
		// working. It is not used by the algorithm!
		double timeCounter = problem.getMaxtime();

		// Start coordinates
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();

		// Maximum satisfaction per time for each iteration.
		double maxSatisfactionPerTime = 0.0;
		// Maximum satisfaction per stamina for each iteration.
		double maxSatisfactionPerStamina = 0.0;

		// The solution for the problem instance
		solution = new SASTPSolution(problem);
		HashMap<Double, Spot> spots = problem.getSpots();

		// Running as long as there is a possibility to visit more Spots.
		boolean running = true;
		// Resting at start not possible
		boolean restingAllowed = false;

		/*
		 * Iterates from the current position through all spots and methods and
		 * selects the spot with the best satisfaction per time and satisfaction
		 * per stamina relations, and which do not break the time and stamina
		 * constraints.
		 */
		// Running as long as there is a possibility to visit more Spots.
		while (running) {
			// List of possible next stops, so called candidates.
			ArrayList<Candidate> candidates = new ArrayList<Candidate>();
			// Iteration through all spots.
			for (double spotKey : spots.keySet()) {
				// Next possible spot.
				Spot possibleNextSpot = spots.get(spotKey);
				// If the spot has been already visited, skip it.
				if (!solution.isSpotAlreadyVisited(possibleNextSpot)) {
					// Satisfaction penalty of the travel.
					double satisfactionPenalty = problem
							.getTravelSatisfactionCost(currentX, currentY,
									possibleNextSpot.getSpotX(),
									possibleNextSpot.getSpotY());
					// Time needed for the travel.
					double travelTime = problem.getTravelTime(currentX,
							currentY, possibleNextSpot.getSpotX(),
							possibleNextSpot.getSpotY());
					HashMap<Double, Method> methods = possibleNextSpot
							.getMethods();

					// Time needed from the next possible spot back to the
					// start.
					double timeBackHome = problem.getTravelTime(
							possibleNextSpot.getSpotX(),
							possibleNextSpot.getSpotY(), problem.getStartX(),
							problem.getStartY());

					// Iteration through all the spot`s methods.
					for (double methodKey : methods.keySet()) {
						// Next possible method of the spot.
						Method possibleNextMethod = methods.get(methodKey);
						// Satisfaction gained through the method.
						double methodSatisfaction = possibleNextMethod
								.getSatisfaction();
						// Stamina cost of the method.
						double methodStaminaPenalty = possibleNextMethod
								.getStamina();
						// Time needed for the method.
						double methodTimePenalty = possibleNextMethod.getTime();

						double satisfaction = methodSatisfaction
								- satisfactionPenalty;
						double timeNeeded = travelTime + methodTimePenalty;

						// Check if the visit of the possible next spot is valid
						if (checkIfSpotPossible(timeNeeded + timeBackHome,
								methodStaminaPenalty, restingAllowed)) {
							// Add the spot, method an resting time to the
							// candidates.
							Candidate cand = new Candidate(possibleNextSpot,
									possibleNextMethod,
									(satisfaction / timeNeeded),
									(satisfaction / methodStaminaPenalty),
									neededRestingTime(
											timeNeeded + timeBackHome,
											methodStaminaPenalty,
											restingAllowed));
							candidates.add(cand);
							// Calculating the the maximum satisfaction per time
							// and satisfaction per stamina of the current
							// iteration.
							if (maxSatisfactionPerTime <= (satisfaction / timeNeeded)) {
								maxSatisfactionPerTime = (satisfaction / timeNeeded);
							}
							if (maxSatisfactionPerStamina <= (satisfaction / methodStaminaPenalty)) {
								maxSatisfactionPerStamina = (satisfaction / methodStaminaPenalty);
							}
						}
					}
				}
			}
			double score;
			double maxScore = 0.0;
			Candidate bestCandidate = null;
			// If no candidates are found the greedy search is over.
			if (!candidates.isEmpty()) {
				// Select the best candidate with the best score.
				for (Candidate cand : candidates) {
					/*
					 * The score is the satisfaction per time normalized with
					 * the maximum satisfaction per time of the current
					 * iteration multiplied with a constant factor and
					 * summarized with the satisfaction per stamina normalized
					 * with the maximum satisfaction per stamina of the current
					 * iteration multiplied with a constant factor.
					 */
					score = (cand.getSattime() / maxSatisfactionPerTime * 10)
							+ (cand.getSatsta() / maxSatisfactionPerStamina * 10);
					if (score > maxScore) {
						maxScore = score;
						bestCandidate = cand;
					}
				}
			} else {
				running = false;
			}

			// If candidates were found.
			if (running) {
				if (restingAllowed) {
					// Add the resting time to the visit before.
					solution.addRest(bestCandidate.getRestTime());
					
					// Update timeCounter, It is not used by the algorithm!
					timeCounter = timeCounter - bestCandidate.getRestTime();
				} else {
					restingAllowed = true;
				}
				// Add the candidate as the next stop, and define it as the
				// current location.
				solution.addStop(bestCandidate.getSpot(),
						bestCandidate.getMethod(), 0.0);
				
				// Update timeCounter, It is not used by the algorithm!
				timeCounter = timeCounter
						- problem.getTravelTime(currentX, currentY,
								bestCandidate.getSpot().getSpotX(),
								bestCandidate.getSpot().getSpotY())
						- bestCandidate.getMethod().getTime();
				
				currentX = bestCandidate.getSpot().getSpotX();
				currentY = bestCandidate.getSpot().getSpotY();

			}
			System.out.println("Time left: "+timeCounter);
		}
	}

	/**
	 * Checks if a spot is valid to visit.
	 * 
	 * @param timeNeeded
	 *            a double, the time needed from the current location to the
	 *            spot, the method time and the travel time back to the start
	 *            point.
	 * @param staminaNeeded
	 *            a double, the stamina needed for the next spot´s method.
	 * @param restingAllowed
	 *            a boolean, whether resting is allowed or not.
	 * @return boolean, whether the next spot is valid to visit.
	 */
	private boolean checkIfSpotPossible(double timeNeeded,
			double staminaNeeded, boolean restingAllowed) {
		if (timeNeeded <= solution.getTimeLeft()
				&& staminaNeeded <= solution.getStaminaLeft()) {
			return true;
		} else if (timeNeeded > solution.getTimeLeft()) {
			return false;
		} else if (restingAllowed) {
			double spareTime = solution.getTimeLeft() - timeNeeded;
			double switchStamina = staminaNeeded - solution.getStaminaLeft();
			// switchTime is the restingTime
			if (switchStamina <= problem.getMaxstamina()) {
				double switchTime = switchStamina / problem.getHabitus();
				if (spareTime >= switchTime) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Checks if a spot is valid to visit and return the needed resting time.
	 * 
	 * @param timeNeeded
	 *            a double, the time needed from the current location to the
	 *            spot, the method time and the travel time back to the start
	 *            point.
	 * @param staminaNeeded
	 *            a double, the stamina needed for the next spot´s method.
	 * @param restingAllowed
	 *            a boolean, whether resting is allowed or not.
	 * @return double, the needed resting time to visit the spot.
	 */
	private double neededRestingTime(double timeNeeded, double staminaNeeded,
			boolean restingAllowed) {
		if (timeNeeded <= solution.getTimeLeft()
				&& staminaNeeded <= solution.getStaminaLeft()) {
			return 0.0;
		} else if (timeNeeded > solution.getTimeLeft()) {
			return 0.0;
		} else if (restingAllowed) {
			double spareTime = solution.getTimeLeft() - timeNeeded;
			double switchStamina = staminaNeeded - solution.getStaminaLeft();
			// switchTime = restingTime
			if (switchStamina <= problem.getMaxstamina()) {
				double switchTime = switchStamina / problem.getHabitus();
				if (spareTime >= switchTime) {
					return switchTime;
				} else {
					return 0.0;
				}
			} else {
				return 0.0;
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * Represents a valid Spot Method pair, which are candidates for a next
	 * visit.
	 */
	private class Candidate {
		private Spot spot;
		private Method method;
		private double sattime, satsta, restTime;

		public double getRestTime() {
			return restTime;
		}

		public Spot getSpot() {
			return spot;
		}

		public Method getMethod() {
			return method;
		}

		public double getSattime() {
			return sattime;
		}

		public double getSatsta() {
			return satsta;
		}

		private Candidate(Spot spot, Method method, double sattime,
				double satsta, double restTime) {
			this.spot = spot;
			this.method = method;
			this.sattime = sattime;
			this.satsta = satsta;
			this.restTime = restTime;
		}
	}
}
