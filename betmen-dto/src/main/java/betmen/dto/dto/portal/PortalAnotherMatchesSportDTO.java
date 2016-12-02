package betmen.dto.dto.portal;

import betmen.dto.dto.SportKindDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PortalAnotherMatchesSportDTO {
    private SportKindDTO sport;
    private List<PortalAnotherMatchesCategoryDTO> categories;
}
