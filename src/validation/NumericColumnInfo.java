package validation;

public class NumericColumnInfo extends ColumnInfo {
   public NumericColumnInfo(String columnName, int min, int max) {
      super(columnName, new NumericFieldValidator(min, max));
   }
   
   public double getMin() {
      return min;
   }
   public void setMin(double min) {
      this.min = min;
   }
   public double getMax() {
      return max;
   }
   public void setMax(double max) {
      this.max = max;
   }
   public double getMean() {
      return mean;
   }
   public void setMean(double mean) {
      this.mean = mean;
   }
   public double getMedian() {
      return median;
   }
   public void setMedian(double median) {
      this.median = median;
   }
   public double getMode() {
      return mode;
   }
   public void setMode(double mode) {
      this.mode = mode;
   }
   public double getStandardDeviation() {
      return standardDeviation;
   }
   public void setStandardDeviation(double standardDeviation) {
      this.standardDeviation = standardDeviation;
   }

   double min;
   double max;
   double mean;
   double median;
   double mode;
   double standardDeviation;
}
