package validation;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexValidator implements FieldValidator {
   public RegexValidator(String regex) {
      pattern = Pattern.compile(regex);
   }
   
   @Override
   public boolean validate(String value) {
      Matcher m = pattern.matcher(value);
      return m.matches();
   }
   
   private Pattern pattern;
}
