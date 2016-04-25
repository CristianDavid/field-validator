package configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import validation.ColumnInfo;
import validation.NumericColumnInfo;
import validation.NominalColumnInfo;
import validation.RegexValidator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ConfigReader {
   public ConfigReader(String filename) {
      columns = new ArrayList<ColumnInfo>();
      try (FileReader inputFile = new FileReader(filename);
           CSVParser  parser    = new CSVParser(inputFile, CSVFormat.EXCEL)) {
         List<CSVRecord> records  = parser.getRecords();
         ColumnInfo columnInfo;
         String columnName;
         String columnType;
         String validatorInfo;
         for (CSVRecord record : records) {
            if (record.size() < 3) {
               String msg = "Row has less than 3 columns!";
               throw new IndexOutOfBoundsException(msg);
            }
            columnName    = record.get(0).trim();
            columnType    = record.get(1).trim();
            validatorInfo = record.get(2).trim();
            switch (columnType) {
               case "numerico":
                  String[] limits = validatorInfo.split(",");
                  int min         = Integer.parseInt(limits[0]);
                  int max         = Integer.parseInt(limits[1]);
                  columnInfo      = new NumericColumnInfo(columnName, min, max);
                  break;
               case "nominal":
                  String[] fields = validatorInfo.split(",");
                  columnInfo      = new NominalColumnInfo(columnName, fields);
                  break;
               case "regex":
                  columnInfo = new ColumnInfo(columnName,
                                             new RegexValidator(validatorInfo));
                  break;
               default:
                  throw new IOException("Column type not supported.");
            }
            columns.add(columnInfo);
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
         System.err.println(e.getMessage());
         System.err.println(filename + "not found");
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println(e.getMessage());
      }
   }
   public ColumnInfo[] getColumns() {
      return columns.toArray(new ColumnInfo[0]);
   }
   private ArrayList<ColumnInfo> columns;
}
