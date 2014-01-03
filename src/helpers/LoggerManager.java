package helpers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Class for managing the logger files
 * 
 */
public class LoggerManager {
	public LoggerManager() {

	}

	/**
	 * Adds a new log line to the logger files
	 * @param filePrefix
	 * @param fileName
	 * @param instanceSize
	 * @param satisfaction
	 * @param timeNeeded
	 * @param staminaLeft
	 * @param runningTimeInSeconds
	 * @param initSatisfaction
	 * @param initRunningTimeInSeconds
	 * @param howOften -->aeot
	 * @throws IOException
	 */
	public void addLog(String filePrefix, String fileName, String instanceSize,
			double satisfaction, double timeNeeded, double staminaLeft,
			double runningTimeInSeconds, double initSatisfaction,
			double initRunningTimeInSeconds, String howOften)
			throws IOException {
		PrintWriter output = new PrintWriter(new FileWriter(filePrefix
				+ "logger/logger.log", true));
		output.println(fileName + " finished. Final satisfaction: "
				+ satisfaction + ", Time needed: " + timeNeeded
				+ ", Stamina left: " + staminaLeft + " Runningtime in sec: "
				+ runningTimeInSeconds);
		output.close();

		PrintWriter output2 = new PrintWriter(new FileWriter(filePrefix
				+ "logger/" + instanceSize + ".log", true));
		output2.println(satisfaction + " " + timeNeeded + " " + staminaLeft
				+ " " + runningTimeInSeconds + " " + initSatisfaction + " "
				+ initRunningTimeInSeconds + " " + howOften);
		output2.close();
	}
}
