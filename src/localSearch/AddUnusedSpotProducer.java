package localSearch;

import java.util.ArrayList;
import helpers.Method;
import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Spot;
import helpers.Stop;

/**
 * Produces a neighbor of a given solution in trying to add an unused spot.
 */
public class AddUnusedSpotProducer {
	public AddUnusedSpotProducer() {

	}

	/**
	 * Produces a neighbor of a given solution in trying to add an unused spot.
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param spot
	 *            Spot the spot to add
	 * @param method
	 *            Method the method to add
	 * @param position
	 *            Integer the position where to add the new spot
	 * @return SASTPSolution a new neighbor
	 */
	public SASTPSolution produceAddUnusedSpotNeighbor(SASTProblem problem,
			SASTPSolution solution, Spot spot, Method method, int position) {
		// sanitizing the input
		if (position < 0 || position >= solution.getTourSize()) {
			return null;
		}

		// Iterate through the tour and add the new spot/method at the position
		int iter = 0;
		SASTPSolution newSolution = new SASTPSolution(problem);
		ArrayList<Stop> newTour = new ArrayList<Stop>();
		while (iter < solution.getTourSize()) {
			solution.setNextStop(iter);
			if (iter == position) {
				newTour.add(new Stop(spot, method, 0.0));
				newTour.add(new Stop(solution.getSpot(), solution.getMethod(),
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
