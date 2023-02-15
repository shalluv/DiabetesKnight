package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVParser {
	public static int[][] readCSV(String filename) {
		try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
			ArrayList<String[]> result = new ArrayList<String[]>();
			String line = "";

			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(",");

				// Verify tokens
				result.add(tokens);
			}
			String[][] result2 = result.toArray(new String[result.size()][]);
			int rowSize = result2.length;
			int colSize = result2[0].length;
			int[][] result3 = new int[rowSize][colSize];
			for (int i = 0; i < rowSize; ++i) {
				for (int j = 0; j < colSize; ++j) {
					result3[i][j] = Integer.parseInt(result2[i][j]);
				}
			}
			return result3;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
