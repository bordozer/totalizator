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
@ToString(of = {"categoryId", "categoryName"})
public class CategoryEditDTO {

    @Min(value = 0)
    private int categoryId;

    @Min(value = 1)
    private int sportKindId;

    @NotBlank(message = "errors.name_should_not_be_blank")
    @Size(min = 3, max = 100, message = "errors.name_has_wrong_length")
    private String categoryName;

    private String logoUrl;

    private String categoryImportId;

    private int remoteGameImportStrategyTypeId;

    public CategoryEditDTO(final int categoryId, final String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
