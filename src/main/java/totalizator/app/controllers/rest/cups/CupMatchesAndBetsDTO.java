package totalizator.app.controllers.rest.cups;

import totalizator.app.dto.CupDTO;

public class CupMatchesAndBetsDTO {

	private CupDTO cup;

	public CupDTO getCup() {
		return cup;
	}

	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}
}
