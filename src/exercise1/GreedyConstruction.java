package exercise1;

import java.util.ArrayList;
import java.util.HashMap;

import helpers.Method;
import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Spot;

public class GreedyConstruction {
	//test
	private SASTProblem problem;
	private SASTPSolution solution;

	public GreedyConstruction(SASTProblem problem) {
		this.problem = problem;
		double timeLeft = problem.getMaxtime();
		double staminaLeft = problem.getInitstamina();
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();
		double maxSatisfactionPerTime = 0.0;
		double maxSatisfactionPerStamina = 0.0;
		solution = new SASTPSolution(problem);
		HashMap<Double, Spot> spots = problem.getSpots();
		boolean running = true;
		boolean restingAllowed = false; // Rest at start not possible

		int counter = 1;
		while (running) {
			ArrayList<Candidate> candidates = new ArrayList<Candidate>();
			for (double spotKey : spots.keySet()) {
				Spot possibleNextSpot = spots.get(spotKey);
				double distance = problem.getDistance(currentX, currentY,
						possibleNextSpot.getSpotX(),
						possibleNextSpot.getSpotY());
				double satisfactionPenalty = problem.getTravelSatisfactionCost(
						currentX, currentY, possibleNextSpot.getSpotX(),
						possibleNextSpot.getSpotY());
				double travelTime = problem.getTravelTime(currentX, currentY,
						possibleNextSpot.getSpotX(),
						possibleNextSpot.getSpotY());
				HashMap<Double, Method> methods = possibleNextSpot.getMethods();
				for (double methodKey : methods.keySet()) {
					Method possibleNextMethod = methods.get(methodKey);
					if (!solution.isSpotMethodAlreadyVisited(possibleNextSpot,
							possibleNextMethod)) {
						double methodSatisfaction = possibleNextMethod
								.getSatisfaction();
						double methodStaminaPenalty = possibleNextMethod
								.getStamina();
						double methodTimePenalty = possibleNextMethod.getTime();

						double timeBackHome = problem.getTravelTime(
								possibleNextSpot.getSpotX(),
								possibleNextSpot.getSpotY(),
								problem.getStartX(), problem.getStartY());

						double satisfaction = methodSatisfaction
								- satisfactionPenalty;
						double timeNeeded = travelTime + methodTimePenalty;

						if (checkIfSpotPossible(timeNeeded + timeBackHome,
								methodStaminaPenalty, restingAllowed)) {
							Candidate cand = new Candidate(possibleNextSpot,
									possibleNextMethod,
									(satisfaction / timeNeeded),
									(satisfaction / methodStaminaPenalty),
									neededRestingTime(
											timeNeeded + timeBackHome,
											methodStaminaPenalty,
											restingAllowed));
							candidates.add(cand);
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
			if (!candidates.isEmpty()) {
				for (Candidate cand : candidates) {
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

			if (running) {
				solution.addStop(bestCandidate.getSpot(),
						bestCandidate.getMethod(), 0.0);
				currentX = bestCandidate.getSpot().getSpotX();
				currentY = bestCandidate.getSpot().getSpotY();
				if (restingAllowed) {
					solution.addRest(bestCandidate.getRestTime());
				} else {
					restingAllowed = true;
				}
			}

			// System.out.println("--->: " + maxScore);
			System.out.println("Counter: " + counter);
			counter++;
		}
		System.out.println("--->: " + solution.getTimeLeft());
		solution.checkSolution();
	}

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
			// switchTime = restingTime
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
