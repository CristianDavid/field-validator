package reports;

import java.util.Arrays;

import org.apache.commons.csv.CSVRecord;

import validation.ColumnInfo;

public class StringDistance {
   public StringDistance(ColumnInfo[] info, CSVRecord[] records) {
      String a = "kitten";
      String b = "sitting";
      Levenshtein dist = new Levenshtein(a, b);
      int d = dist.lev();
      System.out.printf("lev(%s,%s) = %d%n", a, b, d);
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
