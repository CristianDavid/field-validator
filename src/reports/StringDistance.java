package reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import validation.ColumnInfo;
import validation.NominalColumnInfo;

public class StringDistance {
   private static class RecordInfo {
      public RecordInfo() {
         columnName  = "";
         values      = new ArrayList<>(); // TODO Can I do this before java 8 
         suggestions = new ArrayList<>();
         distances   = new ArrayList<>();
      }
      public String columnName;
      public ArrayList<String>  values;
      public ArrayList<String>  suggestions;
      public ArrayList<Integer> distances;
   }
   
   private ArrayList<RecordInfo> outputInfo;
   
   public StringDistance(ColumnInfo[] info, CSVRecord[] records) {
      outputInfo = new ArrayList<>();
      for (int i = 0; i < info.length; i++) {
         if (info[i] instanceof NominalColumnInfo) {
            NominalColumnInfo nominalInfo = (NominalColumnInfo) info[i];
            RecordInfo recordInfo = new RecordInfo();
            recordInfo.columnName = nominalInfo.getColumnName();
            for (int j = 1; j < records.length; j++) {
               String value = records[j].get(i).trim();
               if (!nominalInfo.validate(value)) {
                  int minDist = Integer.MAX_VALUE;
                  String bestSuggestion = "";
                  for (String suggestion : nominalInfo.getValueSet()) {
                     int dist = new Levenshtein(suggestion.trim(), value).lev();
                     if (dist < minDist) {
                        minDist        = dist;
                        bestSuggestion = suggestion;
                     }
                  }
                  recordInfo.values.add(value);
                  recordInfo.suggestions.add(bestSuggestion);
                  recordInfo.distances.add(minDist);
               }
            }
            outputInfo.add(recordInfo);
         }
      }
   }
   
   public void writeToDisk(String filename) {
      try (PrintWriter outFile = new PrintWriter(filename);
            CSVPrinter printer = new CSVPrinter(outFile, CSVFormat.EXCEL)) {
         for (RecordInfo record : outputInfo) {
            printer.printRecord(record.columnName);
            printer.printRecord("Valor", "Sugerencia", "Distancia");
            for (int i = 0; i < record.values.size(); i++) {
               printer.printRecord(
                     record.values.get(i),
                     record.suggestions.get(i),
                     record.distances.get(i));
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println(e.getMessage());
      }
   }
   
   private static class Levenshtein {
      public Levenshtein(String a, String b) {
         this.a = a;
         this.b = b;
         matrix = new int[a.length()+1][b.length()+1];
         for (int[] line : matrix) {
            Arrays.fill(line, -1);
         }
      }
      
      public int lev() {
         return lev(a.length(), b.length());
      }
      
      public int lev(int i, int j) {
         if (matrix[i][j] != -1) return matrix[i][j];
         if (i == 0)             return matrix[i][j] = j;
         if (j == 0)             return matrix[i][j] = i;
         int up     = lev(i-1, j) + 1;
         int left   = lev(i, j-1) + 1;
         int upLeft = lev(i-1, j-1) + (a.charAt(i-1) != b.charAt(j-1)? 1 : 0);
         return matrix[i][j] = Math.min(upLeft, Math.min(up, left));
      }
      
      private int[][] matrix;
      private String  a;
      private String  b;
   }
   public static void main(String[] args) {
      new StringDistance(null, null);
   }
}
