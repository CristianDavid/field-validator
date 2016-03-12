package validation;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.csv.CSVParser;

public class CSVValidation {
   public static void main(String[] args) {
      try {
         FileReader    in = new FileReader("income.csv");
         CSVParser parser = new CSVParser(in);
         for (String[] row : parser.getAllValues()) {
            for (String element : row) {
               System.out.print(element + " ");
            }
            System.out.println();
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
