package betmen.dto.dto.admin;

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
@EqualsAndHashCode
@ToString(of = {"teamId", "teamName"})
public class TeamEditDTO {

    @Min(value = 0)
    private int teamId;

    @NotBlank(message = "errors.name_should_not_be_blank")
    @Size(min = 3, max = 100, message = "errors.name_has_wrong_length")
    private String teamName;

    @Min(value = 1)
    private int categoryId;

    private String teamImportId;

    // GET-only data (wrong design)
    private String teamLogo;
    private boolean teamChecked;
    private int matchCount;
}
