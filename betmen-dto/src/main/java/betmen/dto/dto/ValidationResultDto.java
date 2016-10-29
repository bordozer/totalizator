package betmen.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResultDto {
    private final boolean passed;
    private final String message;
}
