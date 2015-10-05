package totalizator.app.controllers.rest.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.CupDTO;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PortalPageDTO {

	private List<CupDTO> cupsToShow;
	private List<CupDTO> cupsTodayToShow;

	private String portalPageDate;

	public void setCupsToShow( final List<CupDTO> cupsToShow ) {
		this.cupsToShow = cupsToShow;
	}

	public List<CupDTO> getCupsToShow() {
		return cupsToShow;
	}

	public List<CupDTO> getCupsTodayToShow() {
		return cupsTodayToShow;
	}

	public void setCupsTodayToShow( final List<CupDTO> cupsTodayToShow ) {
		this.cupsTodayToShow = cupsTodayToShow;
	}

	public String getPortalPageDate() {
		return portalPageDate;
	}

	public void setPortalPageDate( final String portalPageDate ) {
		this.portalPageDate = portalPageDate;
	}
}
