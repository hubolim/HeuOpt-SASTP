package localSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Stop;

/**
 * Produces a neighbor of a given solution with using the Or Opt method
 */
public class OrOptProducer {
	public OrOptProducer() {

	}

	/**
	 * Produces a neighbor of a given solution with using the Or Opt method
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
	public SASTPSolution produceOrOptNeighbor(SASTProblem problem,
			SASTPSolution solution, int first, int second) {
		return produceOrOptNeighbor(problem, solution, first, second, 0);
	}

	/**
	 * Produces a neighbor of a given solution with using the Or Opt methodot.
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param first
	 *            Integer the position where to start the first edge exchange
	 * @param second
	 *            Integer the position where to start the second edge exchange
	 * @param betweenC
	 *            Integer the number of Stops between the edge exchange
	 * @return SASTPSolution a new neighbor
	 */
	public SASTPSolution produceOrOptNeighbor(SASTProblem problem,
			SASTPSolution solution, int first, int second, int betweenC) {
		// if (first - 1 == second + 1 || first == second + 1 || first == second
		// || first + 1 == second || first + 2 == second
		// || first + 2 >= solution.getTourSize()
		// || second + 1 >= solution.getTourSize() || first - 1 < 0
		// || second < 0) {
		// return null;
		// }

		// sanitizing the input
		if (second + 1 >= first - 1 && second <= first + 1 + betweenC
				|| first + 1 + betweenC >= solution.getTourSize()
				|| second + 1 >= solution.getTourSize() || first - 1 < 0
				|| second < 0) {
			return null;
		}

		// Iterate through the tour and perform or opt at the position first and
		// second
		int iter = 0;
		SASTPSolution newSolution = new SASTPSolution(problem);
		boolean between = false;
		Deque<Stop> queue = new ArrayDeque<Stop>();
		ArrayList<Stop> newTour = new ArrayList<Stop>();
		while (iter < solution.getTourSize()) {
			solution.setNextStop(iter);
			if (iter == first - 1) {
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
				between = true;
			} else if (iter == first + 1 + betweenC) {
				between = false;
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
			} else if (iter == second) {
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
						0.0));
				while (!queue.isEmpty()) {
					newTour.add(queue.remove());
				}
			} else if (between) {
				queue.add(new Stop(solution.getSpot(), solution.getMethod(),
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
