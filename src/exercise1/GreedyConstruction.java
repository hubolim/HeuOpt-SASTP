package exercise1;

import java.util.ArrayList;
import java.util.HashMap;

import helpers.Method;
import helpers.SASTProblem;
import helpers.Spot;

public class GreedyConstruction {

	public GreedyConstruction(SASTProblem problem) {
		double timeLeft = problem.getMaxtime();
		double staminaLeft = problem.getInitstamina();
		double currentX = problem.getStartX();
		double currentY = problem.getStartY();
		double maxSatisfactionPerTime = 0.0;
		double maxSatisfactionPerStamina = 0.0;
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();

		HashMap<Double, Spot> spots = problem.getSpots();

		for (double spotKey : spots.keySet()) {
			Spot possibleNextSpot = spots.get(spotKey);
			double distance = problem.getDistance(currentX, currentY,
					possibleNextSpot.getSpotX(), possibleNextSpot.getSpotY());
			double satisfactionPenalty = problem.getTravelSatisfactionCost(
					currentX, currentY, possibleNextSpot.getSpotX(),
					possibleNextSpot.getSpotY());
			double travelTime = problem.getTravelTime(currentX, currentY,
					possibleNextSpot.getSpotX(), possibleNextSpot.getSpotY());
			HashMap<Double, Method> methods = possibleNextSpot.getMethods();
			for (double methodKey : methods.keySet()) {
				Method possibleNextMethod = methods.get(methodKey);
				double methodSatisfaction = possibleNextMethod
						.getSatisfaction();
				double methodStaminaPenalty = possibleNextMethod.getStamina();
				double methodTimePenalty = possibleNextMethod.getTime();

				double timeBackHome = problem.getTravelTime(
						possibleNextSpot.getSpotX(),
						possibleNextSpot.getSpotY(), problem.getStartX(),
						problem.getStartY());

				double satisfaction = methodSatisfaction - satisfactionPenalty;
				double timeNeeded = travelTime + methodTimePenalty;

				if ((timeNeeded + timeBackHome) <= timeLeft
						&& methodStaminaPenalty <= staminaLeft) {
					Candidate cand = new Candidate(possibleNextSpot.getID(),
							possibleNextMethod.getID(),
							(satisfaction / timeNeeded),
							(satisfaction / methodStaminaPenalty));
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
		double score;
		double maxScore = 0.0;
		Candidate bestCandidate = null;
		for (Candidate cand : candidates) {
			score = (cand.getSattime() / maxSatisfactionPerTime * 10)
					+ (cand.getSatsta() / maxSatisfactionPerStamina * 10);
			if(score > maxScore) {
				maxScore = score;
				bestCandidate = cand;
			}
		}
		System.out.println("--->: "+maxScore);
	}

	private class Candidate {
		private double spotID, methodID, sattime, satsta;

		public double getSpotID() {
			return spotID;
		}

		public double getMethodID() {
			return methodID;
		}

		public double getSattime() {
			return sattime;
		}

		public double getSatsta() {
			return satsta;
		}

		private Candidate(double spotID, double methodID, double sattime,
				double satsta) {
			this.spotID = spotID;
			this.methodID = methodID;
			this.sattime = sattime;
			this.satsta = satsta;
		}
	}
}
