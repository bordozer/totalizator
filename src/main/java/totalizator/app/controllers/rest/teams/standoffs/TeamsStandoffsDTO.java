package totalizator.app.controllers.rest.teams.standoffs;

import totalizator.app.dto.CupDTO;

import java.util.List;

public class TeamsStandoffsDTO {

	private final List<CupDTO> cupsToShow;

	public TeamsStandoffsDTO( final List<CupDTO> cupsToShow ) {
		this.cupsToShow = cupsToShow;
	}

	public List<CupDTO> getCupsToShow() {
		return cupsToShow;
	}
}
