package reports;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import validation.ColumnInfo;
import validation.NumericColumnInfo;

public class Stats {
	private static final Object [] FILE_HEADER_NUMERIC = {"Name","Max","Min","Mode","Mean","Std Deviation"};
	private static final Object [] FILE_HEADER_NOMINAL = {"Name","Frequency"};
	
	double  maximum [], minimum[],mode[],mean[],stdDeviation[];
	ArrayList<ArrayList<String>> listsOfErrors;
	ArrayList<ArrayList<String>> listsOfSuccesses;
	ArrayList<ArrayList<Integer>> listsOfNumberOfSuccesses;
	ArrayList<ArrayList<Integer>> listsOfNumberOfErrors;
	ColumnInfo information[];

	public Stats(ColumnInfo[] info, CSVRecord[] records) {
		information = info;
		maximum = new double [information.length];
		minimum = new double [information.length];
		mode    = new double [information.length];
		mean    = new double [information.length];
		stdDeviation = new double [information.length];
		listsOfNumberOfErrors    = new ArrayList<ArrayList<Integer>>();
		listsOfNumberOfSuccesses = new ArrayList<ArrayList<Integer>>();
		listsOfErrors    = new ArrayList<ArrayList<String>>();
		listsOfSuccesses = new ArrayList<ArrayList<String>>();
		for (int i = 0 ; i< info.length; i++){
			if (info[i] instanceof NumericColumnInfo){
			    NumericColumnInfo numInfo = (NumericColumnInfo) info[i];
				int sum=0;
				ArrayList<Double> numbers = new ArrayList<>();
				for (int w = 1 ; w < records.length; w++){
					String value =records[w].get(i).trim();
					if (numInfo.validate(value)){
						double number=Integer.parseInt(value); 
						sum+=number;
						numbers.add(number);			
					}
				}
				mean[i]    = sum/records.length;
				maximum[i] = Collections.max(numbers);
				minimum[i] = Collections.min(numbers);
				Collections.sort(numbers);
				mode[i]    = mode(numbers);
				stdDeviation[i] = getStdDev(numbers,i);
				numInfo.setMin(minimum[i]);
				numInfo.setMode(mode[i]);
				numInfo.setMax(maximum[i]);
				numInfo.setMean(mean[i]);
				numInfo.setStandardDeviation(stdDeviation[i]);
			}
			else{
				ArrayList<String> errors = new ArrayList<String>();
				ArrayList<Integer> numberOfErrors = new ArrayList<Integer>();
				ArrayList<String> successes = new ArrayList<String>();
				ArrayList<Integer> numberOfSuccesses = new ArrayList<Integer>();

				ArrayList<String> list=new ArrayList<String>();
				for (int j = 1 ; j < records.length; j++){
					String value =records[j].get(i).trim();
					list.add(value); 
					if (information[i].validate(value)){
						if (!successes.contains(value)){
							successes.add(value);
						}
					}
					else{
						if (!errors.contains(value)){
							errors.add(value);							
						}

					}
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
			temp += (a-mean[i])*(a-mean[i]);
		return Math.sqrt(temp/data.size()) ;
	}

	private static double mode(ArrayList<Double> a) {
		double maxCount=0,maxValue=0;
		for (int i = 0 ; i < a.size(); i++){
			if (Collections.frequency(a, a.get(i))> maxCount ){
				maxValue= a.get(i);
				maxCount=Collections.frequency(a, a.get(i));
			}
		}

		return maxValue;
	}

	public void writeToDisk(String filename) {
		try (PrintWriter outFile = new PrintWriter(filename);
				CSVPrinter printer = new CSVPrinter(outFile, CSVFormat.EXCEL)){
			int temp=0;
			for (int i = 0 ; i < information.length; i++) {
				printer.printRecord("---","---","---");
				printer.printRecord(information[i].getColumnName());;
				printer.printRecord("---","---","---");
				if (information[i] instanceof NumericColumnInfo){
					List<String> lst = new ArrayList<String>();
					printer.printRecord(FILE_HEADER_NUMERIC);
					lst.add(information[i].getColumnName());
					lst.add(String.valueOf(maximum[i]));
					lst.add(String.valueOf(minimum[i]));
					lst.add(String.valueOf(mode[i]));
					lst.add(String.valueOf(mean[i]));
					lst.add(String.valueOf(stdDeviation[i]));
					printer.printRecord(lst);
					printer.println();
				}
				else{
					printer.printRecord("SUCCESSES");
					printer.printRecord(FILE_HEADER_NOMINAL);
					for (int w = 0 ; w < listsOfSuccesses.get(temp).size(); w++){
						List<String> lst = new ArrayList<String>();
						String value=listsOfSuccesses.get(temp).get(w);
						String numberOfOcurrences=String.valueOf(listsOfNumberOfSuccesses.get(temp).get(w));						
						lst.add(value);
						lst.add(numberOfOcurrences);
						printer.printRecord(lst);
						lst.clear();
					}
					printer.println();
					printer.printRecord("ERRORS");
					printer.printRecord(FILE_HEADER_NOMINAL);
					for (int w = 0 ; w < listsOfErrors.get(temp).size(); w++){
						List<String> lst = new ArrayList<String>();
						String value=listsOfErrors.get(temp).get(w);
						String numberOfOcurrences=String.valueOf(listsOfNumberOfErrors.get(temp).get(w));						
						lst.add(value);
						lst.add(numberOfOcurrences);
						printer.printRecord(lst);
						lst.clear();
					}
					printer.println();
					temp++;
				}
			}
			printer.flush();
			printer.close();
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} 
	}
}
