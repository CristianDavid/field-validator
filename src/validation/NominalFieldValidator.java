package validation;

import java.util.TreeSet;
import java.util.Arrays;

public class NominalFieldValidator implements FieldValidator {
   private final TreeSet<String> values;
   
   public NominalFieldValidator(String[] values) {
      this.values = new TreeSet<String>(Arrays.asList(values));
   }
   
   @Override
   public boolean validate(String value) {
      try {
         return values.contains(value);
      } catch (NullPointerException e) {
         return false;
      }
   }

}
