package betmen.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FavoriteCategoryDTO {
    private int categoryId;
    private String categoryName;
    private String logoUrl;
    private SportKindDTO sportKind;
    private boolean favoriteCategory;

    public FavoriteCategoryDTO(final int categoryId, final String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
