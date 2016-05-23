package validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import configuration.ConfigReader;
import reports.Correlation;
import reports.Normalization;
import reports.Stats;
import reports.StringDistance;

public class CSVValidation {
   private static String RESULTS_DIRECTORY = "results";
   
	public static void main(String[] args) {
		try (FileReader in     = new FileReader("income.csv");
		     CSVParser  parser = new CSVParser(in, CSVFormat.EXCEL);) {
			ConfigReader config = new ConfigReader("config.csv");
			ColumnInfo[] columnsInfo = config.getColumns();
			List<CSVRecord> values = parser.getRecords();
			CSVRecord[] recordArray = values.toArray(new CSVRecord[0]);
			System.out.println("Processing wait...");
			Stats       stats       = new Stats(columnsInfo, recordArray);
			Correlation correlation = new Correlation(columnsInfo, recordArray);
			StringDistance distance = new StringDistance(columnsInfo, recordArray);
			Normalization normalizer = new Normalization(columnsInfo, recordArray);
			stats.writeToDisk(RESULTS_DIRECTORY + File.separator + "Stats.csv");
			correlation.writeToDisk(RESULTS_DIRECTORY + File.separator +"correlation.csv");
			distance.writeToDisk(RESULTS_DIRECTORY + File.separator +"Suggestions.csv");
			normalizer.writeToDisk(RESULTS_DIRECTORY + File.separator +"normalization.csv");
			System.out.println("Finished");

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
