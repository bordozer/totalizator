package betmen.dto.dto.portal;

import betmen.dto.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCategoryBetStatisticsDTO {
    private CategoryDTO category;
    private int matchesCount;
    private int betsCount;
}
