package validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import configuration.ConfigReader;

public class CSVValidation {
	
	public static void main(String[] args) {
		try (FileReader in = new FileReader("income.csv");
		     CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);) {
			ConfigReader config = new ConfigReader("config.csv");
			ColumnInfo[] informationOfColumns = config.getColumns();
			List<CSVRecord> values = parser.getRecords();
			// validando las columnas
			int j = 0;
			int errorCount = 0;
			for (CSVRecord csvRecord : values) {
				for (int i = 0; i < csvRecord.size(); i++) {
				    if (j == 0) break;
				    String value = csvRecord.get(i).trim();
					if (!informationOfColumns[i].validate(value)) {
					   System.out.printf("Error %d %d: %s", j+1, i+1, value);
					   System.out.println();
					   errorCount++;
					}
                }
				j++;
			}
			System.out.println("Error count: " + errorCount);
			parser.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}
