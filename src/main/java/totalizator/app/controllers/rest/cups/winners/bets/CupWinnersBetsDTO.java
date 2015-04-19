package totalizator.app.controllers.rest.cups.winners.bets;

import totalizator.app.dto.CupTeamBetDTO;

import java.util.List;

public class CupWinnersBetsDTO {

	private final int winnersCount;
	private final List<CupTeamBetDTO> cupBets;

	public CupWinnersBetsDTO( final int winnersCount, final List<CupTeamBetDTO> cupBets ) {
		this.winnersCount = winnersCount;
		this.cupBets = cupBets;
	}

	public List<CupTeamBetDTO> getCupBets() {
		return cupBets;
	}

	public int getWinnersCount() {
		return winnersCount;
	}
}
