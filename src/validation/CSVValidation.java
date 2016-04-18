package validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import configuration.ConfigReader;
import reports.Stats;
import reports.Correlation;

public class CSVValidation {
	
	public static void main(String[] args) {
		try (FileReader in     = new FileReader("income.csv");
		     CSVParser  parser = new CSVParser(in, CSVFormat.EXCEL);) {
			ConfigReader config = new ConfigReader("config.csv");
			ColumnInfo[] columnsInfo = config.getColumns();
			List<CSVRecord> values = parser.getRecords();
			// validando las columnas
			int errorCount = 0;
			for (int i = 1; i < values.size(); i++) {
			   CSVRecord csvRecord = values.get(i);
			   for (int j = 0; j < csvRecord.size(); j++) {
				    String value = csvRecord.get(j).trim();
					if (!columnsInfo[j].validate(value)) {
					   System.out.printf("Error %d %d: %s", i+1, j+1, value);
					   System.out.println();
					   errorCount++;
					}
                }
			}
			System.out.println("Error count: " + errorCount);
			
			CSVRecord[] recordArray = values.toArray(new CSVRecord[0]);
			Stats       stats       = new Stats(columnsInfo, recordArray);
			Correlation correlation = new Correlation(columnsInfo, recordArray);
			correlation.writeToDisk();
			stats.writeToDisk();
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		} catch (IndexOutOfBoundsException e) {
		   e.printStackTrace();
           System.err.println(e.getMessage());
		}
	}
}
