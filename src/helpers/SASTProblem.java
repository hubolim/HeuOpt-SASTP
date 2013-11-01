package helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SASTProblem {

	public SASTProblem(String inputLocation) throws IOException {
		FileReader fr = new FileReader(inputLocation);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
