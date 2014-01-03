package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LogAnalyser {

	public void analyseLog(int i, String filename) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(filename))));
		String line;
		double satisfaction = 0.0;
		double timeNeeded = 0.0;
		double staminaLeft = 0.0;
		double runningTimeInSec = 0.0;
		double initSatisfaction = 0.0;
		double initRunningTimeInSec = 0.0;
		double addUnused = 0.0;
		double exchange = 0.0;
		double orOpt = 0.0;
		double twoOpt = 0.0;
		double count = 0.0;
		double bestSatis = 0.0;
		double initbestSatis = 0.0;
		ArrayList<Double> satis = new ArrayList<Double>();
		ArrayList<Double> initsatis = new ArrayList<Double>();
		while ((line = bufferedReader.readLine()) != null) {
			String[] splittedInput = line.split("\\s+");
			double curSatis = Double.parseDouble(splittedInput[0]);
			if (curSatis > bestSatis) {
				bestSatis = curSatis;
			}
			satis.add(curSatis);
			satisfaction = satisfaction + curSatis;
			timeNeeded = timeNeeded + Double.parseDouble(splittedInput[1]);
			staminaLeft = staminaLeft + Double.parseDouble(splittedInput[2]);
			runningTimeInSec = runningTimeInSec
					+ Double.parseDouble(splittedInput[3]);
			double initSatis = Double.parseDouble(splittedInput[4]);
			if (initSatis > initbestSatis) {
				initbestSatis = initSatis;
			}
			initsatis.add(initSatis);
			initSatisfaction = initSatisfaction + initSatis;
			initRunningTimeInSec = initRunningTimeInSec
					+ Double.parseDouble(splittedInput[5]);
			// aeot
			addUnused = addUnused + Double.parseDouble(splittedInput[6]);
			exchange = exchange + Double.parseDouble(splittedInput[7]);
			orOpt = orOpt + Double.parseDouble(splittedInput[8]);
			twoOpt = twoOpt + Double.parseDouble(splittedInput[9]);
			count++;
		}
		a.put(i, addUnused / count);
		e.put(i, exchange / count);
		o.put(i, orOpt / count);
		t.put(i, twoOpt / count);

		double xx = 0.0;
		for (int j = 0; j < satis.size(); j++) {
			double curS = satis.get(j) - (satisfaction / count);
			xx = xx + (curS * curS);
		}
		double dev = Math.sqrt(xx / count);
		time.put(i, runningTimeInSec / count);
		avgScore.put(i, satisfaction / count);
		bestScore.put(i, bestSatis);
		deviation.put(i, dev);

		double xx1 = 0.0;
		for (int j = 0; j < satis.size(); j++) {
			double curS = initsatis.get(j) - (initSatisfaction / count);
			xx1 = xx1 + (curS * curS);
		}
		double dev1 = Math.sqrt(xx1 / count);
		inittime.put(i, initRunningTimeInSec / count);
		initavgScore.put(i, initSatisfaction / count);
		initbestScore.put(i, initbestSatis);
		initdeviation.put(i, dev1);

		bufferedReader.close();
	}

	private HashMap<Integer, Double> time = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> avgScore = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> bestScore = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> deviation = new HashMap<Integer, Double>();

	private HashMap<Integer, Double> inittime = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> initavgScore = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> initbestScore = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> initdeviation = new HashMap<Integer, Double>();

	private HashMap<Integer, Double> a = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> e = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> o = new HashMap<Integer, Double>();
	private HashMap<Integer, Double> t = new HashMap<Integer, Double>();

	private double getVal(HashMap<Integer, Double> map, int key) {
		if (map.containsKey(key)) {
			return (double) Math.round(map.get(key) * 100000) / 100000;
		} else {
			return 0.0;
		}
	}

	public void printLatexLog(String header, int init) {
		String xx = "\\hline \\multirow{3}{*}{" + header + "}";
		if (init == 1) {
			// Avg. Time
			xx = xx + " &Avg. Time";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(inittime, i);
			}
			xx = xx + "\\\\";

			// Best Satisf.
			xx = xx + " &Best Satisf.";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(initbestScore, i);
			}
			xx = xx + "\\\\";

			// Avg. Satisf.
			xx = xx + " &Avg. Satisf.";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(initavgScore, i);
			}
			xx = xx + "\\\\";

			// Deviation.
			xx = xx + " &Deviation";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & \\pm" + getVal(initdeviation, i);
			}
			xx = xx + "\\\\";
		} else if (init == 2) {
			// Avg. Time
			xx = xx + " &Avg. Time";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(time, i);
			}
			xx = xx + "\\\\";

			// Best Satisf.
			xx = xx + " &Best Satisf.";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(bestScore, i);
			}
			xx = xx + "\\\\";

			// Avg. Satisf.
			xx = xx + " &Avg. Satisf.";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(avgScore, i);
			}
			xx = xx + "\\\\";

			// Deviation.
			xx = xx + " &Deviation";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & \\pm" + getVal(deviation, i);
			}
			xx = xx + "\\\\";
		} else {
			// Avg. Time
			xx = xx + " &AddUnused";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(a, i);
			}
			xx = xx + "\\\\";

			// Best Satisf.
			xx = xx + " &ExchangeSpot";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(e, i);
			}
			xx = xx + "\\\\";

			// Avg. Satisf.
			xx = xx + " &OrOpt";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & " + getVal(o, i);
			}
			xx = xx + "\\\\";

			// Deviation.
			xx = xx + " &2Opt";
			for (int i = 1; i <= 7; i++) {
				xx = xx + " & \\pm" + getVal(t, i);
			}
			xx = xx + "\\\\";
		}
		System.out.println(xx);
	}
}
