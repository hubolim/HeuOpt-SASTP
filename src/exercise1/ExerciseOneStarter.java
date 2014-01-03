package exercise1;

import helpers.InvalidInputException;
import helpers.LoggerManager;
import helpers.SASTPSolution;
import helpers.SASTProblem;

import java.io.IOException;
import java.io.PrintWriter;

import localSearch.LocalSearch;
import metaHeuristic.VND;
import construction.GreedyConstruction;

public class ExerciseOneStarter {

	public void runExercise1(String inputLocationPrefix, String instanceSize,
			String iteration, boolean initRandom) {
		// String inputLocation = "instances/sastp_100.prob";
		SASTProblem sastProblem = null;
		LoggerManager logMan = new LoggerManager();
		try {
			sastProblem = new SASTProblem(inputLocationPrefix
					+ "instances/sastp_" + instanceSize + ".prob");
			// System.out.println(sastProblem);
		} catch (IOException e) {
			System.out
					.println("Input file not found or input file is corrupted");
		} catch (InvalidInputException dd) {
			System.out.println("Invalid input, check the input file format");
		} catch (NumberFormatException nfe) {
			System.out.println("Invalid input, check the input file format");
		}
		if (sastProblem != null) {
			double startTime = (double) System.currentTimeMillis();
			GreedyConstruction gc = new GreedyConstruction();
			SASTPSolution bestGreedy = gc.performGreedyConstruction(
					sastProblem, initRandom);
			if (initRandom) {
				for (int i = 0; i < 50; i++) {
					SASTPSolution currentGreedy = gc.performGreedyConstruction(
							sastProblem, true);
					if (currentGreedy.getFinalSatisfaction() > bestGreedy
							.getFinalSatisfaction()) {
						bestGreedy = currentGreedy;
					}
				}
			}
			double initEndTime = (double) System.currentTimeMillis();
			LocalSearch ls = new LocalSearch();
			if (bestGreedy.checkSolution()) {
				System.out.println("The produced init solution is valid.");
				System.out.println(bestGreedy.getFinalSatisfaction());
			} else {
				System.out.println("The produced init solution is invalid!");
			}
			VND vnd = new VND();
			// SASTPSolution solution = vnd.performVND(sastProblem, bestGreedy,
			// 1,
			// inputLocationPrefix + "solutions/sastp_" + instanceSize
			// + "_" + iteration + ".sol");
			SASTPSolution solution = vnd.performVND(sastProblem,
					bestGreedy, 0, inputLocationPrefix + "solutions/sastp_"
							+ instanceSize + "_" + iteration + ".sol");
			double endTime = (double) System.currentTimeMillis();
			if (solution.checkSolution()) {
				System.out.println("The produced solution is valid.");
				System.out.println(solution.getFinalSatisfaction());
				solution.saveSolution(inputLocationPrefix + "solutions/sastp_"
						+ instanceSize + "_" + iteration + ".sol");
				try {
					logMan.addLog(
							inputLocationPrefix,
							"sastp_" + instanceSize + "_" + iteration + ".sol",
							instanceSize,
							solution.getFinalSatisfaction(),
							sastProblem.getMaxtime()
									- solution.getFinalTimeLeft(),
							solution.getStaminaLeft(),
							((endTime - startTime) / 1000.0),
							bestGreedy.getFinalSatisfaction(),
							((initEndTime - startTime) / 1000.0),
							vnd.getHowOftenLSUsed());
				} catch (IOException e) {
					System.out.println("Problem in saving to the logger file.");
				}
			} else {
				System.out.println("The produced solution is invalid!");
			}
		}
	}
}
