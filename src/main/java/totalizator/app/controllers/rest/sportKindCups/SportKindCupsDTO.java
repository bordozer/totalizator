package totalizator.app.controllers.rest.sportKindCups;

import totalizator.app.dto.CupDTO;
import totalizator.app.dto.SportKindDTO;

import java.util.List;

public class SportKindCupsDTO {

	private SportKindDTO sportKind;
	private List<CupDTO> cups;

	public SportKindCupsDTO( final SportKindDTO sportKind ) {
		this.sportKind = sportKind;
	}

	public SportKindDTO getSportKind() {
		return sportKind;
	}

	public void setSportKind( final SportKindDTO sportKind ) {
		this.sportKind = sportKind;
	}

	public void setCups( final List<CupDTO> cups ) {
		this.cups = cups;
	}

	public List<CupDTO> getCups() {
		return cups;
	}
}
