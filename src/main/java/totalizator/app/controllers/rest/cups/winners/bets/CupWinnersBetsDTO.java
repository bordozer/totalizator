package totalizator.app.controllers.rest.cups.winners.bets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.CupTeamBetDTO;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupWinnersBetsDTO {

	private int winnersCount;
	private List<CupTeamBetDTO> cupBets;

	public CupWinnersBetsDTO() {
	}

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	public List<CupTeamBetDTO> getCupBets() {
		return cupBets;
	}

	public void setCupBets( final List<CupTeamBetDTO> cupBets ) {
		this.cupBets = cupBets;
	}
}
