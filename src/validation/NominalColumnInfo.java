package validation;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class NominalColumnInfo extends ColumnInfo {
   public NominalColumnInfo(String columnName, String[] values) {
      super(columnName, new NominalFieldValidator(values));
      valueSet = values;
   }
   
   /*
    * This function returns a list containing all different values
    * that are valid if you use the validate() method,
    * the list is unmodifiable so attempting to change it's content
    * will raise an OperationNotSupportedException.
    */
   public List<String> getValueSet() {
      return Collections.unmodifiableList(Arrays.asList(valueSet));
   }
   
   private String[] valueSet;
}
