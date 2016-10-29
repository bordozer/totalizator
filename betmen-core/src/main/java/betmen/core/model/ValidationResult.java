package betmen.core.model;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public class ValidationResult {

    private final boolean passed;
    private final String errorCode;
    private final String translatedMessage;

    public static ValidationResult pass() {
        return new ValidationResult(true, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    public static ValidationResult fail(final String errorCode, final String translatedMessage) {
        return new ValidationResult(false, errorCode, translatedMessage);
    }

    private ValidationResult(final boolean passed, final String errorCode, final String translatedMessage) {
        this.passed = passed;
        this.errorCode = errorCode;
        this.translatedMessage = translatedMessage;
    }
}
