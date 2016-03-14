package validation;

import java.util.TreeSet;
import java.util.Arrays;
import java.util.Comparator;

public class NominalFieldValidator implements FieldValidator {
   private final TreeSet<String> values;
   
   public NominalFieldValidator(String[] values) {
      this.values = new TreeSet<String>(
         new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
               return a.compareToIgnoreCase(b);
            }
         });
      this.values.addAll(Arrays.asList(values));
      this.values.add("");
      this.values.add("?");
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
