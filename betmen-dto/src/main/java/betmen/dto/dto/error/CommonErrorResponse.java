package betmen.dto.dto.error;

import com.beust.jcommander.internal.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CommonErrorResponse {
    private List<CommonErrorResource> errors;

    public CommonErrorResponse(final CommonErrorResource errorResource) {
        this.errors = Lists.newArrayList(errorResource);
    }

    public Long errorsCount() {
        return Optional.ofNullable(errors)
                .map(errors1 -> errors1.stream().count())
                .orElse(0L);

    }

    public boolean containsError(final String errorCode) {
        return Optional.ofNullable(errors)
                .map(errors1 -> errors1.stream()
                        .filter(error -> error.getErrorCode().equals(errorCode))
                        .findFirst())
                .orElse(Optional.empty()).isPresent();
    }
}
