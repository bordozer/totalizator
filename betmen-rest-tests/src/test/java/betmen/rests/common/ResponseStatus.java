package betmen.rests.common;

import org.apache.http.HttpStatus;

public enum ResponseStatus {

    UNAUTHORIZED(HttpStatus.SC_UNAUTHORIZED)
    , OK(HttpStatus.SC_OK)
    , BAD_REQUEST(HttpStatus.SC_BAD_REQUEST)
    , UNPROCESSABLE_ENTITY(HttpStatus.SC_UNPROCESSABLE_ENTITY)
    , BUSINESS_EXCEPTION(HttpStatus.SC_EXPECTATION_FAILED)
    ;

    private final int code;

    ResponseStatus(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
