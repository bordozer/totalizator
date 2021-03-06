package betmen.dto.dto.portal;

import betmen.dto.dto.CupDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalPageDTO {
    private List<CupDTO> cupsToShow;
    private List<CupDTO> cupsTodayToShow;
    private String portalPageDate;
    private List<PortalAnotherMatchesSportDTO> anotherMatchesOnDate;
}
