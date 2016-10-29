package betmen.web.controllers.rest.sportKindCups;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.SportKindDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SportKindCupsDTO {
    private SportKindDTO sportKind;
    private List<CupDTO> cups;

    public SportKindCupsDTO(final SportKindDTO sportKind) {
        this.sportKind = sportKind;
    }
}
