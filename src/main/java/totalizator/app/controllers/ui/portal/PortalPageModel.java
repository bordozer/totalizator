package totalizator.app.controllers.ui.portal;

import totalizator.app.dto.CupDTO;

import java.util.List;

public class PortalPageModel {

	private String userName;
	private List<CupDTO> cupsToShow;
	private String cupsToShowJSON;

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

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
