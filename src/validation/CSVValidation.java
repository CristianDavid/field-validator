package validation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVValidation {
	public static int NUM_COLUMNS = 15;
	public static int NUM_ROWS = 50;
	public static String typesOfWorkingClass[] = { "State-gov", "Local-gov", "Private", "Self-emp-not-inc", "Self-emp-inc",
			"Federal-gov", "Never-worked", "Without-pay" };
	public static String typesOfEducation[] = { "10th", "11th", "1st-4th", "5th-6th", "7th-8th", "9th", "Assoc-acdm",
			"Assoc-voc", "Bachelors", "Doctorate", "HS-grad", "Masters", "Preschool", "Prof-school", "Some-college" };
	public static String typesOfMaritalStatus[] = { "Never-married", "Married-civ-spouse", "Divorced",
			"Married-spouse", "absent", "Separated", "Married-AF-spouse", "Widowed" };
	public static String typesOfOccupations[] = { "Adm-clerical", "Exec-managerial", "Handlers", "cleaners",
			"Prof-specialty", "Other-service", "Sales", "Craft-repair", "Transport-moving", "Farming-fishing",
			"Machine-op-inspct", "Tech-support", "Protective-serv", "Armed-Forces" };
	public static String typesOfRelationships[] = { "Not-in-family", "Husband", "Wife", "Own-child", "Unmarried",
			"Other-relative" };
	public static String typesOfRaces[] = { "White", "Black", "Asian-Pac-Islander", "Amer-Indian-Eskimo",
			"Other" };
	public static String typesOfSexes[] = { "Male", "Female" };
	public static String typesOfNatianalities[] = { "United-States", "Cambodia", "England", "Puerto-Rico",
	   "Canada", "Germany", "Outlying-US", "India", "Japan",
	   "Greece", "South", "China", "Cuba", "Iran", "Honduras", "Philippines", "Italy",
	   "Poland", "Jamaica", "Vietnam", "Mexico", "Portugal", "Ireland", "France",
	   "Dominican-Republic", "Laos", "Ecuador", "Taiwan", "Haiti", "Columbia",
	   "Hungary", "Guatemala", "Nicaragua", "Scotland", "Thailand", "Yugoslavia",
	   "El-Salvador", "Trinadad&Tobago", "Peru", "Hong", "Holand-Netherlands" };
	public static String classes[] = { "<=50K", ">50K" };

	public static void main(String[] args) {
		try {
			FileReader in = new FileReader("income.csv");
			CSVParser parser = new CSVParser(in, CSVFormat.EXCEL);
			ColumnInfo informationOfColumns[] = new ColumnInfo[NUM_COLUMNS];
			// Tipos de validadores para las columnInfo
			FieldValidator validadores[] = new FieldValidator[NUM_COLUMNS];
			validadores[0] = new NumericFieldValidator(0, 999);
			validadores[1] = new NominalFieldValidator(typesOfWorkingClass);
			validadores[2] = new NumericFieldValidator(0, 25555555);
			validadores[3] = new NominalFieldValidator(typesOfEducation);
			validadores[4] = new NumericFieldValidator(0, 17);
			validadores[5] = new NominalFieldValidator(typesOfMaritalStatus);
			validadores[6] = new NominalFieldValidator(typesOfOccupations);
			validadores[7] = new NominalFieldValidator(typesOfRelationships);
			validadores[8] = new NominalFieldValidator(typesOfRaces);
			validadores[9] = new NominalFieldValidator(typesOfSexes);
			validadores[10] = new NumericFieldValidator(0, 10000000);
			validadores[11] = new NumericFieldValidator(0, 10000000);
			validadores[12] = new NumericFieldValidator(0, 7 * 24);
			validadores[13] = new NominalFieldValidator(typesOfNatianalities);
			validadores[14] = new NominalFieldValidator(classes);
			List<CSVRecord> values = parser.getRecords();
			for (int i = 0; i < NUM_COLUMNS; i++) {
				informationOfColumns[i] = new ColumnInfo(values.get(0).get(i), validadores[i]);
			}

			// validando las columnas
			int j = 0;
			int errorCount = 0;
			for (CSVRecord csvRecord : values) {
				for (int i = 0; i < NUM_COLUMNS; i++) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
