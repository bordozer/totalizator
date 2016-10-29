package betmen.dto.dto.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {
    private String field;
    private String rejectedValue;
    private String errorCode;

    @Override
    public String toString() {
        return String.format("%s: %s", field, errorCode);
    }
}
