package totalizator.app.controllers.rest.user.card;

import totalizator.app.dto.CupDTO;

import java.util.List;

public class UserCardDTO {

	private List<CupDTO> cupsToShow;

	public UserCardDTO( final List<CupDTO> cupsToShow ) {
		this.cupsToShow = cupsToShow;
	}

	public void setCupsToShow( final List<CupDTO> cupsToShow ) {
		this.cupsToShow = cupsToShow;
	}

	public List<CupDTO> getCupsToShow() {
		return cupsToShow;
	}
}
