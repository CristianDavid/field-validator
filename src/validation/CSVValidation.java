package validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	public static void main(String[] args) {
		try (FileReader in     = new FileReader("income.csv");
		     CSVParser  parser = new CSVParser(in, CSVFormat.EXCEL);) {
			ConfigReader config = new ConfigReader("config.csv");
			ColumnInfo[] columnsInfo = config.getColumns();
			List<CSVRecord> values = parser.getRecords();
			CSVRecord[] recordArray = values.toArray(new CSVRecord[0]);
			System.out.println("Generating statistics");
			Stats       stats       = new Stats(columnsInfo, recordArray);
			System.out.println("Calculating correlation values");
			Correlation correlation = new Correlation(columnsInfo, recordArray);
			System.out.println("Generating suggestions");
			StringDistance distance = new StringDistance(columnsInfo, recordArray);
			System.out.println("Writing statistics to disk");
			stats.writeToDisk("Stats.csv");
			System.out.println("Writing correlation to disk");
			correlation.writeToDisk("correlation.csv");
			System.out.println("Writing suggestions to disk");
			distance.writeToDisk("Suggestions.csv");
			System.out.println("Doing the normalization");
			Normalization normalizer = new Normalization(columnsInfo, recordArray);
			normalizer.writeToDisk("normalization.csv");
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
