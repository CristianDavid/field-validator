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
   Correlation(ColumnInfo[] info, CSVRecord[] records) {
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
                  for (String r : getDifferentValues(i, records)) {
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
               for (CSVRecord record : records) {
                  double ai = getAsDouble(record.get(i));
                  double bi = getAsDouble(record.get(j));
                  res += (ai*bi) - records.length*meanA*meanB;
               }
               res /= records.length*stdDevA*stdDevB;
            }
            correlation.add(new Double(res));
         }
      }
   }
   
   void writeToDisk() {
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
      for (CSVRecord record : records) {
         if (!used.contains(record.get(a))) {
            used.add(record.get(a));
            values.add(record.get(a));
         }
      }
      return values;
   }
   
   private double expectedFrequencie(String c, String r, int a, int b,
         CSVRecord[] records) {
      int countA = 0;
      int countB = 0;
      for (CSVRecord record : records) {
         if (record.get(a).equals(c)) countA++;
         if (record.get(b).equals(r)) countB++;
      }
      return countA * countB / (double) records.length;
   }

   private double observedFrequencie(String c, String r, int a, int b,
         CSVRecord[] records) {
      int count = 0;
      for (CSVRecord record : records) {
         if (record.get(a).equals(c) && record.get(b).equals(r)) count++;
      }
      return (double)count;
   }

   
   private double mean(int a, CSVRecord[] records) {
      double res = 0.0;
      for (int i = 0; i < records.length; i++) {
         double val = getAsDouble(records[i].get(a));
         res += val;
      }
      res /= (double)records.length;
      return res;
   }
   
   private double stdDev(int a, double mean, CSVRecord[] records) {
      double res = 0.0;
      for (int i = 0; i < records.length; i++) {
         double val = getAsDouble(records[i].get(a));
         res += Math.pow(val - mean, 2.0);
      }
      res /= (double)records.length;
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
