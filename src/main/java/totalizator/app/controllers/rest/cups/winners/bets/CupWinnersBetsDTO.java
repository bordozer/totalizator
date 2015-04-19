package totalizator.app.controllers.rest.cups.winners.bets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.TeamDTO;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupWinnersBetsDTO {

	private int winnersCount;
	private List<TeamDTO> winners;
	private List<UserCupBetsDTO> usersCupBets;

	public CupWinnersBetsDTO() {
	}

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	public List<TeamDTO> getWinners() {
		return winners;
	}

	public void setWinners( final List<TeamDTO> winners ) {
		this.winners = winners;
	}

	public List<UserCupBetsDTO> getUsersCupBets() {
		return usersCupBets;
	}

	public void setUsersCupBets( final List<UserCupBetsDTO> usersCupBets ) {
		this.usersCupBets = usersCupBets;
	}
}
