package localSearch;

import java.util.ArrayList;
import java.util.Random;

import helpers.Method;
import helpers.SASTPSolution;
import helpers.SASTProblem;
import helpers.Spot;

/**
 * Class for performing local searches. Use the method performLocalSearch
 * 
 * @see performLocalSearch
 */
public class LocalSearch {

	private AddUnusedSpotProducer addUnusedProducer = new AddUnusedSpotProducer();
	private ExchangeSpotProducer exchangeSpotProducer = new ExchangeSpotProducer();
	private OrOptProducer orOptProducer = new OrOptProducer();
	private TwoOptProducer twoOptProducer = new TwoOptProducer();
	private int iterOne = 0;
	private int iterTwo = 0;

	/**
	 * Private method for producing a neighbor
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param aeot
	 *            Integer determining which local search will be used, needs to
	 *            be between 0(AddUnusedSpot), 1(ExchangeSpot), 2(OrOpt Range1),
	 *            3(TwoOpt), 4(OrOpt Range2)
	 * @param rnb
	 *            Integer determining the selection process needs to be 0(random
	 *            neighbor), 1(next improvement) or 2(best Improvement)
	 * @param iter1
	 *            Integer the position where to start the first edge exchange
	 * @param iter2
	 *            Integer the position where to start the second edge exchange
	 * @return SASTPSolution a new neighbor or null if no valid solution can be
	 *         produced
	 */
	private SASTPSolution produceNeighbor(SASTProblem problem,
			SASTPSolution solution, int aeot, int rnb, int iter1, int iter2) {
		SASTPSolution neighbor = null;
		ArrayList<Spot> unusedSpots = solution.getUnusedSpots();
		// AddUnusedSpot || ExchangeSpot
		// All Methods for the new spot have to be checked
		if (aeot == 0 || aeot == 1) {
			SASTPSolution bestSolution = null;
			ArrayList<SASTPSolution> neighbors = new ArrayList<SASTPSolution>();
			int i1 = 0;
			while (i1 < unusedSpots.size()) {
				Spot spot = unusedSpots.get(i1);
				ArrayList<Method> methods = new ArrayList<Method>(spot
						.getMethods().values());
				int i2 = 0;
				while (i2 < unusedSpots.size()) {
					// AddUnusedSpot
					if (aeot == 0) {
						neighbor = addUnusedProducer
								.produceAddUnusedSpotNeighbor(problem,
										solution, spot, methods.get(i2), iter1);
					}
					// ExchangeSpot
					else if (aeot == 1) {
						neighbor = exchangeSpotProducer
								.produceExchangeSpotNeighbor(problem, solution,
										spot, methods.get(i2), iter1);
					}
					if (rnb == 0) {
						if (neighbor.getFinalSatisfaction() > solution
								.getFinalSatisfaction()
								&& neighbor.checkSolution()) {
							neighbors.add(neighbor);
						}
						if (neighbors.size() == 5) {
							Random rand = new Random();
							int random = rand.nextInt(neighbors.size());
							return neighbors.get(random);
						}
					} else if (rnb == 1) {
						if (neighbor.getFinalSatisfaction() > solution
								.getFinalSatisfaction()
								&& neighbor.checkSolution()) {
							return neighbor;
						}
					} else if (rnb == 2) {
						if (neighbor.getFinalSatisfaction() > solution
								.getFinalSatisfaction()
								&& neighbor.checkSolution()) {
							bestSolution = neighbor;
						}
					}
					i2++;
				}
				i1++;
			}
			if (rnb == 0 && !neighbors.isEmpty()) {
				Random rand = new Random();
				int random = rand.nextInt(neighbors.size());
				return neighbors.get(random);
			} else if (rnb == 2) {
				return bestSolution;
			}
		}
		// OrOpt 1
		else if (aeot == 2) {
			neighbor = orOptProducer.produceOrOptNeighbor(problem, solution,
					iter1, iter2);
		}
		// OrOpt 2
		else if (aeot == 4) {
			neighbor = orOptProducer.produceOrOptNeighbor(problem, solution,
					iter1, iter2, 1);
		}
		// TwoOpt
		else if (aeot == 3) {
			neighbor = twoOptProducer.produceTwoOptNeighbor(problem, solution,
					iter1, iter2);
		}
		return neighbor;
	}

	/**
	 * Method for performing a Local Search
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param aeot
	 *            Integer determining which local search will be used, needs to
	 *            be between 0(AddUnusedSpot), 1(ExchangeSpot), 2(OrOpt) and
	 *            3(TwoOpt)
	 * @param rnb
	 *            Integer determining the selection process needs to be 0(random
	 *            neighbor), 1(next improvement) or 2(best Improvement)
	 * @return SASTPSolution a new neighbor or null if no valid solution can be
	 *         produced
	 */
	public SASTPSolution performLocalSearch(SASTProblem problem,
			SASTPSolution solution, int aeot, int rnb) {
		ArrayList<SASTPSolution> neighbors = new ArrayList<SASTPSolution>();
		int iter1 = 0;
		SASTPSolution compareBestNeighbor = solution;
		SASTPSolution currentBestNeighbor = null;
		while (iter1 < solution.getTourSize()) {
			int iter2 = 0;
			int iter2Count = 1;
			if (aeot == 2 || aeot == 3) {
				iter2Count = solution.getTourSize();
			}
			while (iter2 < iter2Count) {
				SASTPSolution neighbor = produceNeighbor(problem, solution,
						aeot, rnb, iter1, iter2);
				if (neighbor != null) {
					if (neighbor.checkSolution()) {
						// random neighbor
						if (rnb == 0) {
							if (neighbor.getFinalSatisfaction() > solution
									.getFinalSatisfaction()) {
								neighbors.add(neighbor);
							}
							if (neighbors.size() == 5) {
								Random rand = new Random();
								int random = rand.nextInt(neighbors.size());
								return neighbors.get(random);
							}
						}
						// next improvement
						else if (rnb == 1) {
							if (neighbor.getFinalSatisfaction() > solution
									.getFinalSatisfaction()) {
								return neighbor;
							}
						}
						// best improvement
						else if (rnb == 2) {
							if (neighbor.getFinalSatisfaction() > compareBestNeighbor
									.getFinalSatisfaction()) {
								currentBestNeighbor = neighbor;
								compareBestNeighbor = neighbor;
							}
						}
					}
				}
				iter2++;
			}
			iter1++;
		}
		if (rnb == 0 && !neighbors.isEmpty()) {
			Random rand = new Random();
			int random = rand.nextInt(neighbors.size());
			return neighbors.get(random);
		} else if (rnb == 2) {
			return currentBestNeighbor;
		}
		return null;
	}

	/**
	 * Method for performing a Local Search (faster)
	 * 
	 * @param problem
	 *            SASTProblem problem instance
	 * @param solution
	 *            SASTPSolution the basic solution of the problem instance
	 * @param aeot
	 *            Integer determining which local search will be used, needs to
	 *            be between 0(AddUnusedSpot), 1(ExchangeSpot), 2(OrOpt) and
	 *            3(TwoOpt)
	 * @param rnb
	 *            Integer determining the selection process needs to be 0(random
	 *            neighbor), 1(next improvement) or 2(best Improvement)
	 * @return SASTPSolution a new neighbor or null if no valid solution can be
	 *         produced
	 */
	public SASTPSolution performLocalSearchMemory(SASTProblem problem,
			SASTPSolution solution, int aeot, int rnb) {
		rnb = 1;
		while (iterOne < solution.getTourSize()) {
			if(iterTwo == solution.getTourSize()) {
				iterTwo = 0;
			}
			while (iterTwo < solution.getTourSize()) {
				SASTPSolution neighbor = produceNeighbor(problem, solution,
						aeot, rnb, iterOne, iterTwo);
				if (neighbor != null) {
					if (neighbor.checkSolution()) {
						// next improvement
						if (rnb == 1) {
							if (neighbor.getFinalSatisfaction() > solution
									.getFinalSatisfaction()) {
								return neighbor;
							}
						}
					}
				}
				iterTwo++;
			}
			iterOne++;
		}
		return null;
	}

	public void resetIter() {
		iterOne = 0;
		iterTwo = 0;
	}
}
