package configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import validation.ColumnInfo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ConfigReader {
   public ConfigReader(String filename) {
      columns = new ArrayList<ColumnInfo>();
      try (FileReader inputFile = new FileReader(filename);
           CSVParser  parser    = new CSVParser(inputFile, CSVFormat.EXCEL)) {
         List<CSVRecord> records = parser.getRecords();
         ColumnInfo column = null;
         String columnName = null;
         String columnType = null;
         for (CSVRecord record : records) {
            columnName = record.get(0).trim();
            columnType = record.get(1).trim();
            switch (columnType) {
               case "numerico":
                  break;
               case "nominal":
                  break;
               case "regex":
                  break;
               default:
                  throw new IOException("Column type not supported.");
            }
            columns.add(column);
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
