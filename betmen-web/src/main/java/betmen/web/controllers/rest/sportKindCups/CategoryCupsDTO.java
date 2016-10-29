package betmen.web.controllers.rest.sportKindCups;

import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.CupDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryCupsDTO {
    private CategoryDTO category;
    private List<CupDTO> cups;
}
