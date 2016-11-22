package betmen.web.controllers.rest.sportKindCups;

import betmen.dto.dto.CupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CupAndCategoryFavoriteDTO {
    private final CupDTO cup;
    private final boolean categoryInFavorite;
}
