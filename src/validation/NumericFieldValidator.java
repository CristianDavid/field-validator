package validation;

public class NumericFieldValidator implements FieldValidator {
   public final int min;
   public final int max;
   
   public NumericFieldValidator(int min, int max) {
      this.min = min;
      this.max = max;
   }
   
   @Override
   public boolean validate(String value) {
      try {
         int val = Integer.parseInt(value);
         return min <= val && val <= max;
      } catch (NumberFormatException e) {
         return false;
      }
   }

}
