package betmen.dto.dto.admin;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SportKindEditDTO {

    @Min(value = 0)
    private int sportKindId;

    @NotBlank(message = "errors.name_should_not_be_blank")
    @Size(min = 3, max = 100, message = "errors.name_has_wrong_length")
    private String sportKindName;
}
