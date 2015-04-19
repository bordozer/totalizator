package totalizator.app.controllers.rest.cups.winners.bets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.UserDTO;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupWinnersBetsDTO {

	private int winnersCount;
	private Map<UserDTO, List<CupTeamBetDTO>> usersCupBets;

	public CupWinnersBetsDTO() {
	}

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	public void setUsersCupBets( final Map<UserDTO, List<CupTeamBetDTO>> usersCupBets ) {
		this.usersCupBets = usersCupBets;
	}

	public Map<UserDTO, List<CupTeamBetDTO>> getUsersCupBets() {
		return usersCupBets;
	}
}
