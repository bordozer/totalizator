package totalizator.app.beans;

import org.apache.commons.lang.StringUtils;

public class ValidationResult {

	private final boolean passed;
	private final String message;

	private ValidationResult( final boolean passed, final String message ) {
		this.passed = passed;
		this.message = message;
	}

	public static ValidationResult pass() {
		return new ValidationResult( true, StringUtils.EMPTY );
	}

	public static ValidationResult fail( final String message ) {
		return new ValidationResult( true, message );
	}

	public boolean isPassed() {
		return passed;
	}

	public String getMessage() {
		return message;
	}
}
