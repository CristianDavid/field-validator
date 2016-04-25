package validation;

public class ColumnInfo {
	private final String columnName;
	private final FieldValidator validator;

	public ColumnInfo(String columnName, FieldValidator validator) {
		if (validator == null) throw new NullPointerException();
		this.columnName = columnName;
		this.validator  = validator;
	}

	public boolean isNumeric() {
		return validator instanceof NumericFieldValidator;
	}

	public boolean isNominal() {
		return validator instanceof NominalFieldValidator;
	}

	public boolean validate(String value) {
		return validator.validate(value);
	}

	public String getColumnName() {
		return columnName;
	}
}
