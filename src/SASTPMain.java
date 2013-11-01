import java.io.IOException;

import helpers.SASTProblem;


public class SASTPMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello");
		String inputLocation = "instances/sastp_10.prob";
		try {
			SASTProblem sastProblem = new SASTProblem(inputLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
