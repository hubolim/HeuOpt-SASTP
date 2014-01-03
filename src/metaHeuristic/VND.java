package metaHeuristic;

import java.util.HashMap;

import localSearch.LocalSearch;
import helpers.SASTPSolution;
import helpers.SASTProblem;

/**
 * Class for performing a variable neighborhood search on
 * 
 */
public class VND {

	private HashMap<Integer, Integer> howOften = new HashMap<Integer, Integer>();

	/**
	 * 
	 * @param problem
	 *            SASTProblem representing the problem instance
	 * @param solution
	 *            SASTPSolution the initial solution
	 * @param rnb
	 *            int 0(random) 1(next improvement) 2(best improvement)
	 * @return SASTPSolution the best neighbor found
	 */
	public SASTPSolution performVND(SASTProblem problem,
			SASTPSolution solution, int rnb, String filename) {
		boolean running = true;
		int teoa = 0;
		SASTPSolution currentNeighbor = solution;
		SASTPSolution nextNeighbor = null;
		LocalSearch ls = new LocalSearch();
		howOften.put(0, 0);
		howOften.put(1, 0);
		howOften.put(2, 0);
		howOften.put(3, 0);
		howOften.put(4, 0);
		while (running) {
			int aeot = 0;
			// TwoOpt
			if (teoa == 0) {
				System.out.println("2Opt");
				aeot = 3;
			}
			// ExchangeSpot
			else if (teoa == 1) {
				System.out.println("ExchangeSpot");
				aeot = 1;
			}
			// OrOpt
			else if (teoa == 2) {
				System.out.println("OrOpt");
				aeot = 2;
			}
			// AddSpot
			else if (teoa == 3) {
				System.out.println("AddSpot");
				aeot = 0;
			}
			// OrOpt 2
			// else if (teoa == 4) {
			// System.out.println("OrOpt 2");
			// }
			else {
				running = false;
			}
			nextNeighbor = ls.performLocalSearch(problem, currentNeighbor,
					aeot, rnb);
			if (nextNeighbor != null) {
				currentNeighbor = nextNeighbor;
				int o = howOften.get(aeot) + 1;
				howOften.put(aeot, o);
				System.out.println(currentNeighbor.getFinalSatisfaction());
				currentNeighbor.saveSolution(filename);
				teoa = 0;
			} else {
				teoa++;
			}
		}
		return currentNeighbor;
	}
	
	/**
	 * 
	 * @param problem
	 *            SASTProblem representing the problem instance
	 * @param solution
	 *            SASTPSolution the initial solution
	 * @param rnb
	 *            int 0(random) 1(next improvement) 2(best improvement)
	 * @return SASTPSolution the best neighbor found
	 */
	public SASTPSolution performVNDMemory(SASTProblem problem,
			SASTPSolution solution, int rnb, String filename) {
		boolean running = true;
		boolean betterFound = false;
		int teoa = 0;
		SASTPSolution currentNeighbor = solution;
		SASTPSolution nextNeighbor = null;
		LocalSearch ls = new LocalSearch();
		howOften.put(0, 0);
		howOften.put(1, 0);
		howOften.put(2, 0);
		howOften.put(3, 0);
		howOften.put(4, 0);
		while (running) {
			int aeot = 0;
			// TwoOpt
			if (teoa == 0) {
				System.out.println("2Opt");
				aeot = 3;
			}
			// ExchangeSpot
			else if (teoa == 1) {
				System.out.println("ExchangeSpot");
				aeot = 1;
			}
			// OrOpt
			else if (teoa == 2) {
				System.out.println("OrOpt");
				aeot = 2;
			}
			// AddSpot
			else if (teoa == 3) {
				System.out.println("AddSpot");
				aeot = 0;
			}
			// OrOpt 2
			// else if (teoa == 4) {
			// System.out.println("OrOpt 2");
			// }
			else {
				running = false;
			}
			nextNeighbor = ls.performLocalSearchMemory(problem, currentNeighbor,
					aeot, rnb);
			if (nextNeighbor != null) {
				currentNeighbor = nextNeighbor;
				int o = howOften.get(aeot) + 1;
				howOften.put(aeot, o);
				System.out.println(currentNeighbor.getFinalSatisfaction());
				currentNeighbor.saveSolution(filename);
				betterFound = true;
			} else {
				teoa++;
				ls.resetIter();
				if(teoa > 3 && betterFound) {
					teoa = 0;
					betterFound = false;
				}
			}
		}
		return currentNeighbor;
	}

	public String getHowOftenLSUsed() {
		// aeot
		return howOften.get(0) + " " + howOften.get(1) + " " + howOften.get(2)
				+ " " + howOften.get(3);
	}
}
