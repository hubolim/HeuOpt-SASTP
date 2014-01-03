package localSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import helpers.Method;
import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Spot;
import helpers.Stop;

/**
 * Produces a neighbor of a given solution with using the 2-Opt method
 */
public class TwoOptProducer {
	public TwoOptProducer() {

	}

	/**
	 * Produces a neighbor of a given solution with using the 2-Opt method
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param first
	 *            Integer the position where to start the first edge exchange
	 * @param second
	 *            Integer the position where to start the second edge exchange
	 * @return SASTPSolution a new neighbor
	 */
	public SASTPSolution produceTwoOptNeighbor(SASTProblem problem,
			SASTPSolution solution, int first, int second) {

		// sanitizing the input
		if (first == second || first + 1 == second || first == second + 1
				|| first + 1 >= solution.getTourSize()
				|| second + 1 >= solution.getTourSize() || first < 0
				|| second < 0) {
			return null;
		}

		// Iterate through the tour and perform 2-opt at the position first and
		// second
		int iter = 0;
		SASTPSolution newSolution = new SASTPSolution(problem);
		boolean invert = false;
		Deque<Stop> stack = new ArrayDeque<Stop>();
		ArrayList<Stop> newTour = new ArrayList<Stop>();
		while (iter < solution.getTourSize()) {
			solution.setNextStop(iter);
			if (iter == first + 1) {
				invert = true;
				stack.push(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
			} else if (iter == second) {
				invert = false;
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
				while (!stack.isEmpty()) {
					newTour.add(stack.pop());
				}
			} else if (invert) {
				stack.push(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
			} else {
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
			}
			iter++;
		}

		// Update the new tour (needed stamina, resting time)
		iter = 0;
		while (iter < newTour.size()) {
			// If enough stamina is available, simply add the stop
			if (newSolution.getStaminaLeft() >= newTour.get(iter).getMethod()
					.getStamina()) {
				newSolution.addStop(newTour.get(iter).getSpot(),
						newTour.get(iter).getMethod(), 0.0);
			}
			// If not enough stamina is available, add a rest
			else {
				// Calculating the needed resting time
				double staminaNeeded = newTour.get(iter).getMethod()
						.getStamina()
						- newSolution.getStaminaLeft();
				double restingTime = staminaNeeded / problem.getHabitus();
				newSolution.addRest(restingTime);
				newSolution.addStop(newTour.get(iter).getSpot(),
						newTour.get(iter).getMethod(), 0.0);
			}
			iter++;
		}
		return newSolution;
	}
}
