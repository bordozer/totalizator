package betmen.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
@SuppressWarnings("serial")
public abstract class BackendException extends RuntimeException {
    private final Collection<String> errors;

    public BackendException(final String message) {
        super(message);
        errors = Collections.singleton(message);
    }

    public BackendException(final Set<String> errors) {
        this.errors = new ArrayList<>(errors);
    }

    public Collection<String> getErrors() {
        return Collections.unmodifiableCollection(errors);
    }
}
