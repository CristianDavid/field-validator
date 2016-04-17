package configuration;
import validation.ColumnInfo;

public class ConfigReader {
   public ConfigReader(String filename) {
      
   }
   public ColumnInfo[] getColumns() {
      return columns;
   }
   private ColumnInfo[] columns;
}
