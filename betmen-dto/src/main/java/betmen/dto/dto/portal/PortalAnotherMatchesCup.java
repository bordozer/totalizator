package betmen.dto.dto.portal;

import betmen.dto.dto.CupItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PortalAnotherMatchesCup {
    private CupItemDTO cup;
    private int matchesCount;
}
