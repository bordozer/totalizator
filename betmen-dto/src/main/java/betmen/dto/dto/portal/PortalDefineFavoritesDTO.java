package betmen.dto.dto.portal;

import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
import betmen.dto.dto.SportKindDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PortalDefineFavoritesDTO {
    private SportKindDTO sport;
    private List<FavoriteCategoryDTO> categories;
}
