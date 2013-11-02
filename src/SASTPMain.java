import java.io.IOException;

import helpers.InvalidInputException;
import helpers.SASTProblem;

public class SASTPMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputLocation = "instances/sastp_10.prob";
		try {
			SASTProblem sastProblem = new SASTProblem(inputLocation);
			System.out.println(sastProblem);
		} catch (IOException e) {
			System.out
					.println("Input file not found or input file is corrupted");
		} catch (InvalidInputException dd) {
			System.out.println("Invalid input, check the input file format");
		} catch (NumberFormatException nfe) {
			System.out.println("Invalid input, check the input file format");
		}

	}

}
