package reports;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import validation.ColumnInfo;

public class Stats {
	
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final Object [] FILE_HEADER_NUMERIC = {"Name","Max","Min","Mode","Mean","Std Deviation"};
	private static final Object [] FILE_HEADER_NOMINAL = {"Name","Frequency"};
	public static final String FILENAME = "Stats";
	
	double  maximum [], minimum[],mode[],mean[],stdDeviation[];
	ArrayList<ArrayList<String>> listsOfErrors;
	ArrayList<ArrayList<String>> listsOfSuccesses;
	ArrayList<ArrayList<Integer>> listsOfNumberOfSuccesses;
	ArrayList<ArrayList<Integer>> listsOfNumberOfErrors;
	ColumnInfo information[];


	public Stats(ColumnInfo[] info, CSVRecord[] records) {
		information =info;
		maximum = new double [information.length];
		minimum= new double [information.length];
		mode = new double [information.length];
		mean = new double [information.length];
		stdDeviation = new double [information.length];
		listsOfNumberOfErrors= new ArrayList<ArrayList<Integer>>();
		listsOfNumberOfSuccesses= new ArrayList<ArrayList<Integer>>();
		listsOfErrors = new ArrayList<ArrayList<String>>();
		listsOfSuccesses = new ArrayList<ArrayList<String>>();
		for (int i = 0 ; i< info.length; i++){
			if (info[i].isNumeric()){
				int sum=0;
				ArrayList<Double> numbers = new ArrayList<>();
				for (CSVRecord record: records){
					double number=Integer.parseInt(record.get(i).trim()); 
					sum+=number;
					numbers.add(number);
				}
				mean[i] = sum/records.length;
				maximum[i]=Collections.max(numbers);
				minimum[i] = Collections.min(numbers);
				Collections.sort(numbers);
				mode [i]= mode(numbers);
				stdDeviation [i]= getStdDev(numbers,i);
			}
			else{
				ArrayList<String> errors = new ArrayList<String>();
				ArrayList<Integer> numberOfErrors = new ArrayList<Integer>();
				ArrayList<String> successes = new ArrayList<String>();
				ArrayList<Integer> numberOfSuccesses = new ArrayList<Integer>();
				
				ArrayList<String> list=new ArrayList<String>();
				for (CSVRecord record: records){
					list.add(record.get(i));
					if (information[i].validate(record.get(i)))
						successes.add(record.get(i));
					else
						errors.add(record.get(i));
				}
				for (String error : errors)
					numberOfErrors.add(Collections.frequency(list, error));
				for (String success: successes)
					numberOfSuccesses.add(Collections.frequency(list, success));
				listsOfErrors.add(errors); 
				listsOfSuccesses.add(successes);
				listsOfNumberOfSuccesses.add(numberOfSuccesses);
				listsOfNumberOfErrors.add(numberOfErrors);
			}
		}
	}

	private double getStdDev(ArrayList<Double> data,int i){
		double temp = 0;
		for(double a : data)
			temp += (mean[i]-a)*(mean[i]-a);
		return Math.sqrt(temp/data.size()) ;
	}

	private static double mode(ArrayList<Double> a) {
		double maxValue=0, maxCount;
		maxCount = a.size();
		for (int i = 0; i < a.size(); ++i) {
			int count = 0;
			for (int j = 0; j < a.size(); ++j) {
				if (a.get(j)== a.get(i)) ++count;
			}
			if (count > maxCount) {
				maxCount = count;
				maxValue = a.get(i);
			}
		}

		return maxValue;
	}

	public void writeToDisk() {
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
		try {
			fileWriter = new FileWriter(FILENAME);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
			for (int i = 0 ; i < information.length; i++) {
				List<String> lst = new ArrayList<String>();
				if (information[i].isNumeric()){
					csvFilePrinter.printRecord(FILE_HEADER_NUMERIC);
					lst.add(information[i].getColumnName());
					lst.add(String.valueOf(maximum[i]));
					lst.add(String.valueOf(minimum[i]));
					lst.add(String.valueOf(mode[i]));
					lst.add(String.valueOf(mean[i]));
					lst.add(String.valueOf(stdDeviation[i]));
					csvFilePrinter.printRecord(lst);
					csvFilePrinter.println();
				}
				else{
					
					csvFilePrinter.printRecord(FILE_HEADER_NOMINAL);
					csvFilePrinter.printRecord("Succeses");
					for (String succces: listsOfSuccesses.get(i)){
						lst.add(succces);
						lst.add(String.valueOf(Collections.frequency(listsOfSuccesses.get(i), succces)));
						csvFilePrinter.printRecord(lst);
					}
					csvFilePrinter.printRecord("Errors");
					for (String error: listsOfErrors.get(i)){
						lst.add(error);
						lst.add(String.valueOf(Collections.frequency(listsOfErrors.get(i), error)));
						csvFilePrinter.printRecord(lst);
					}
					
				}
				
			}
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}
		}
	}
}
