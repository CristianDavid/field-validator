package reports;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import validation.ColumnInfo;
import validation.NumericColumnInfo;

public class Normalization {
	ColumnInfo[] information;
	CSVRecord[] records;
	public static double CIELING_VALUE = 1;
	public static double FLOOR_VALUE = 0;

	public Normalization(ColumnInfo[] info, CSVRecord[] records) {
		information = info;
		this.records = records;
	}

	public void writeToDisk(String filename) {
		try (PrintWriter outFile = new PrintWriter(filename);
				CSVPrinter printer = new CSVPrinter(outFile, CSVFormat.EXCEL)) {
			printer.printRecord(records[0]);
			for (int i = 0; i < records.length; i++) {
				List<String> list = new ArrayList<String>();
				// Checking for invalid row
				boolean invalidRow = false;
				for (int j = 0; j < records[j].size(); j++) {
					if (information[j] instanceof NumericColumnInfo) {
						if (!information[j].validate(records[i].get(j))) {
							invalidRow = true;
						}
					}
				}
				if (invalidRow)
					continue;
				// printing if is valid
				for (int j = 0; j < records[j].size(); j++) {
					if (information[j] instanceof NumericColumnInfo) {
						NumericColumnInfo info;
						info = (NumericColumnInfo) information[j];
						// min-max formula
						Double value = ( ( Integer.parseInt(records[i].get(j) ) - info.getMin() )) /( info.getMax()
								- info.getMin()) * (CIELING_VALUE - FLOOR_VALUE) + FLOOR_VALUE;
						list.add(String.valueOf(value));
					} else {
						list.add(records[i].get(j));
					}
				}
				printer.printRecord(list);
				list.clear();

			}
			printer.flush();
			printer.close();
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}

}
