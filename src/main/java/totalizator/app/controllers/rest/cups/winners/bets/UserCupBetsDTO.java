package totalizator.app.controllers.rest.cups.winners.bets;

import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.UserDTO;

import java.util.List;

public class UserCupBetsDTO {

	private UserDTO user;
	private List<CupTeamBetDTO> userCupBets;

	public UserDTO getUser() {
		return user;
	}

	public void setUser( final UserDTO user ) {
		this.user = user;
	}

	public List<CupTeamBetDTO> getUserCupBets() {
		return userCupBets;
	}

	public void setUserCupBets( final List<CupTeamBetDTO> userCupBets ) {
		this.userCupBets = userCupBets;
	}
}
