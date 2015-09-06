package totalizator.app.controllers.rest.matches.bets.collapsed;

import totalizator.app.dto.CupDTO;
import totalizator.app.dto.UserDTO;

public class MatchesAndBetsCollapsedDTO {

	private final CupDTO cup;
	private final UserDTO user;

	private int matchesCount;
	private int userBetsCount;
	private int matchesWithoutBetsCount;

	public MatchesAndBetsCollapsedDTO( final CupDTO cupDTO, final UserDTO userDTO ) {
		this.cup = cupDTO;
		this.user = userDTO;
	}

	public CupDTO getCup() {
		return cup;
	}

	public UserDTO getUser() {
		return user;
	}

	public int getMatchesCount() {
		return matchesCount;
	}

	public void setMatchesCount( final int matchesCount ) {
		this.matchesCount = matchesCount;
	}

	public int getUserBetsCount() {
		return userBetsCount;
	}

	public void setUserBetsCount( final int userBetsCount ) {
		this.userBetsCount = userBetsCount;
	}

	public int getMatchesWithoutBetsCount() {
		return matchesWithoutBetsCount;
	}

	public void setMatchesWithoutBetsCount( final int matchesWithoutBetsCount ) {
		this.matchesWithoutBetsCount = matchesWithoutBetsCount;
	}

	@Override
	public String toString() {
		return String.format( "%s: matches %d, %d / %d", cup, matchesCount, userBetsCount, matchesWithoutBetsCount );
	}
}
