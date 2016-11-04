package betmen.web.controllers.rest.sportKindCups;

import betmen.dto.dto.CupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CupAndCategoryFavoriteDTO {
    private final CupDTO cup;
    private final boolean categoryInFavorite;
}
