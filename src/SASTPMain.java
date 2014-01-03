import java.io.IOException;

import analysis.LogAnalyser;

import construction.GreedyConstruction;
import exercise1.ExerciseOneStarter;

import localSearch.LocalSearch;
import metaHeuristic.VND;

import helpers.InvalidInputException;
import helpers.SASTPSolution;
import helpers.SASTProblem;

public class SASTPMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("1")) {
				ExerciseOneStarter ex1 = new ExerciseOneStarter();
				String inputPrefix = "";
				inputPrefix = "/home1/hot0842138/rand/HeuOpt-SASTP/";
				for (int i = 1; i <= 30; i++) {
					ex1.runExercise1(inputPrefix, args[1], "" + i, true);
				}
			} else if (args[0].equals("2")) {
				ExerciseOneStarter ex1 = new ExerciseOneStarter();
				String inputPrefix = "";
				inputPrefix = "/home1/hot0842138/rand/HeuOpt-SASTP/";
				ex1.runExercise1(inputPrefix, args[1], args[2], true);
			}
		}
		String inputPrefix = "";
		inputPrefix = "/home/master/Uni/MasterWS13/Heuristic_Optimization_Techniques/";
		inputPrefix = inputPrefix + "fast/";
		inputPrefix = inputPrefix + "logger/";
		LogAnalyser la = new LogAnalyser();
		try {
			la.analyseLog(1, inputPrefix + "10.log");
			la.analyseLog(2, inputPrefix + "20.log");
			la.analyseLog(3, inputPrefix + "50.log");
			la.analyseLog(4, inputPrefix + "100.log");
			la.analyseLog(5, inputPrefix + "200.log");
			la.analyseLog(6, inputPrefix + "500.log");
			la.analyseLog(7, inputPrefix + "1000.log");

			la.printLatexLog("LocalSearch", 3);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
