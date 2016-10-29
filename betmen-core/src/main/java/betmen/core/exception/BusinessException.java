package betmen.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends BackendException {

    private final String errorCode;
    private final String translatedMessage;

    public BusinessException(final String exceptionMessage, final String errorCode, final String translatedMessage) {
        super(exceptionMessage);
        this.errorCode = errorCode;
        this.translatedMessage = translatedMessage;
    }
}
