package totalizator.app.controllers.rest.cupTeamBets;

import totalizator.app.dto.CupTeamBetDTO;

import java.util.List;

public class CupTeamBetsDTO {

	private List<CupTeamBetDTO> cupTeamBets;

	public CupTeamBetsDTO() {
	}

	public CupTeamBetsDTO( final List<CupTeamBetDTO> cupTeamBets ) {
		this.cupTeamBets = cupTeamBets;
	}

	public List<CupTeamBetDTO> getCupTeamBets() {
		return cupTeamBets;
	}

	public void setCupTeamBets( final List<CupTeamBetDTO> cupTeamBets ) {
		this.cupTeamBets = cupTeamBets;
	}
}
