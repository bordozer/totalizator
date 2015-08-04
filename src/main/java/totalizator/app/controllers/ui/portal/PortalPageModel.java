package totalizator.app.controllers.ui.portal;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.dto.CupDTO;

import java.util.List;

public class PortalPageModel extends AbstractPageModel {

	private List<CupDTO> cupsToShow;
	private String cupsToShowJSON;

	public List<CupDTO> getCupsToShow() {
		return cupsToShow;
	}

	public void setCupsToShow( final List<CupDTO> cupsToShow ) {
		this.cupsToShow = cupsToShow;
	}

	public void setCupsToShowJSON( final String cupsToShowJSON ) {
		this.cupsToShowJSON = cupsToShowJSON;
	}

	public String getCupsToShowJSON() {
		return cupsToShowJSON;
	}
}
