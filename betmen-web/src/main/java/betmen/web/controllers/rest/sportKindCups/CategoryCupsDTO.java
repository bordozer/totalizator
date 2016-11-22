package betmen.web.controllers.rest.sportKindCups;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryCupsDTO {
    private FavoriteCategoryDTO category;
    private List<CupDTO> cups;
}
