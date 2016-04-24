package validation;

public class ColumnInfo {
	private final String columnName;
	private double max;
	private double min;
	private double mode;
	private double mean;
	private double stdDeviation;
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

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMode() {
		return mode;
	}

	public void setMode(double mode) {
		this.mode = mode;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStdDeviation() {
		return stdDeviation;
	}

	public void setStdDeviation(double stdDeviation) {
		this.stdDeviation = stdDeviation;
	}
	
}
