package reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord; 

import validation.ColumnInfo;

public class Correlation {
   public Correlation(ColumnInfo[] info, CSVRecord[] records) {
      attributeA  = new ArrayList<String>();
      attributeB  = new ArrayList<String>();
      correlation = new ArrayList<Double>();
      for (int i = 0; i < info.length; i++) {
         for (int j = 0; j < info.length; j++) {
            attributeA.add(info[i].getColumnName());
            attributeB.add(info[j].getColumnName());
            double res;
            if (info[i].isNominal() || info[j].isNominal()) {
               res = 0.0;
               for (String c : getDifferentValues(i, records)) {
                  for (String r : getDifferentValues(j, records)) {
                     double o = observedFrequencie(c, r, i, j, records);
                     double e = expectedFrequencie(c, r, i, j, records);
                     res += Math.pow(o-e, 2.0) / e;
                  }
               }
            } else { // They're both numerical
               res = 0.0;
               double meanA   = mean(i, records);
               double meanB   = mean(j, records);
               double stdDevA = stdDev(i, meanA, records);
               double stdDevB = stdDev(j, meanB, records);
               for (int k = 1; k < records.length; k++) {
                  CSVRecord record = records[k];
                  double ai = getAsDouble(record.get(i));
                  double bi = getAsDouble(record.get(j));
                  res += (ai-meanA)*(bi-meanB);
               }
               res /= (records.length-1)*stdDevA*stdDevB;
            }
            correlation.add(new Double(res));
         }
      }
   }
   
   public void writeToDisk() {
      try (PrintWriter outFile = new PrintWriter("correlation.csv");
            CSVPrinter printer = new CSVPrinter(outFile, CSVFormat.EXCEL)) {
         printer.printRecord("Atributo A", "Atributo B", "Correlacion");
         for (int i = 0; i < attributeA.size(); i++) {
            printer.printRecord(
                  attributeA.get(i), attributeB.get(i), correlation.get(i));
         }
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println(e.getMessage());
      }
   }
   
   private ArrayList<String> getDifferentValues(int a, CSVRecord []records) {
      ArrayList<String> values = new ArrayList<String>();
      TreeSet<String> used = new TreeSet<String>();
      for (int i = 1; i < records.length; i++) {
         CSVRecord record = records[i];
         String str = record.get(a).trim();
         if (!used.contains(str)) {
            used.add(str);
            values.add(str);
         }
      }
      return values;
   }
   
   private double expectedFrequencie(String c, String r, int a, int b,
         CSVRecord[] records) {
      int countA = 0;
      int countB = 0;
      for (int i = 1; i < records.length; i++) {
         CSVRecord record = records[i];
         if (record.get(a).trim().equals(c)) countA++;
         if (record.get(b).trim().equals(r)) countB++;
      }
      return ((double)countA * countB) / ((double)records.length-1);
   }

   private double observedFrequencie(String c, String r, int a, int b,
         CSVRecord[] records) {
      int count = 0;
      for (int i = 1; i < records.length; i++) {
         CSVRecord record = records[i];
         String strA = record.get(a).trim();
         String strB = record.get(b).trim();
         if (strA.equals(c) && strB.equals(r)) count++;
      }
      return (double)count;
   }

   
   private double mean(int a, CSVRecord[] records) {
      double res = 0.0;
      for (int i = 1; i < records.length; i++) {
         double val = getAsDouble(records[i].get(a));
         res += val;
      }
      res /= ((double)records.length - 1.0);
      return res;
   }
   
   private double stdDev(int a, double mean, CSVRecord[] records) {
      double res = 0.0;
      for (int i = 1; i < records.length; i++) {
         double val = getAsDouble(records[i].get(a));
         res += Math.pow(val - mean, 2.0);
      }
      res /= ((double)records.length-1.0);
      res = Math.sqrt(res);
      return res;
   }
   
   private double getAsDouble(String str) {
      try {
         return Double.parseDouble(str);
      } catch (NumberFormatException e) {
         return 0.0;
      }
   }
   
   private ArrayList<String> attributeA;
   private ArrayList<String> attributeB;
   private ArrayList<Double> correlation;
}
