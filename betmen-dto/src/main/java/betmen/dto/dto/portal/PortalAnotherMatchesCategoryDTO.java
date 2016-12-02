package betmen.dto.dto.portal;

import betmen.dto.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PortalAnotherMatchesCategoryDTO {
    private CategoryDTO category;
    private List<PortalAnotherMatchesCupDTO> cups;
}
