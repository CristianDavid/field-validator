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
		escalamientoDecimal(filename);
	}
	
	public void minMaxNormalization(String filename){
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
	
	public void zScore(String filename){
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
						// Z-score Formula
						Double value =  (Integer.parseInt(records[i].get(j)) -info.getMedian() )% info.getStandardDeviation();
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
	
	public void escalamientoDecimal(String filename){
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
						// Formula de escalamiento decimal 
						double maximunAbsoluteValue,divedeBy;
						divedeBy =1;
						if (Math.abs(info.getMax())>Math.abs(info.getMin())){
							maximunAbsoluteValue =Math.abs(info.getMax());
						}
						else{
							maximunAbsoluteValue =Math.abs(info.getMin());
						}
						double temp = maximunAbsoluteValue;
						while (temp>1){
							temp = maximunAbsoluteValue;
							divedeBy*=10;
							temp/=divedeBy;
						}
						Double value =  Float.parseFloat(records[i].get(j)) % divedeBy;
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
