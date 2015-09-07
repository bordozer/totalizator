package totalizator.app.controllers.rest.matches.bets.collapsed;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class MatchesAndBetsCollapsedDTO {

	private final CupDTO cup;
	private final UserDTO user;

	private int matchesCount;
	private int userBetsCount;
	private int matchesWithoutBetsCount;

	private LocalDateTime firstMatchTime;
	private LocalDateTime firstMatchNoBetTime;

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

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getFirstMatchTime() {
		return firstMatchTime;
	}

	public void setFirstMatchTime( final LocalDateTime firstMatchTime ) {
		this.firstMatchTime = firstMatchTime;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getFirstMatchNoBetTime() {
		return firstMatchNoBetTime;
	}

	public void setFirstMatchNoBetTime( final LocalDateTime firstMatchNoBetTime ) {
		this.firstMatchNoBetTime = firstMatchNoBetTime;
	}

	@Override
	public String toString() {
		return String.format( "%s: matches %d, %d / %d", cup, matchesCount, userBetsCount, matchesWithoutBetsCount );
	}
}
