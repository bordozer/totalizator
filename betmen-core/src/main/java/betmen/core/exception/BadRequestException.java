package betmen.core.exception;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BadRequestException extends BackendException {

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final Set<String> errors) {
        super(errors);
    }

    @Override
    public String getMessage() {
        return Optional.ofNullable(super.getMessage()).orElse(getErrors().stream().collect(Collectors.joining(", ")));
    }
}
