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
	private int nowPlayingMatchesCount;
	private int userBetsCount;
	private int matchesWithoutBetsCount;

	private LocalDateTime firstMatchTime;
	private LocalDateTime firstMatchNoBetTime;
	private int todayMatchesCount;
	private int futureMatchesCount;

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

	public int getNowPlayingMatchesCount() {
		return nowPlayingMatchesCount;
	}

	public void setNowPlayingMatchesCount( final int nowPlayingMatchesCount ) {
		this.nowPlayingMatchesCount = nowPlayingMatchesCount;
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

	public void setTodayMatchesCount( final int todayMatchesCount ) {
		this.todayMatchesCount = todayMatchesCount;
	}

	public int getTodayMatchesCount() {
		return todayMatchesCount;
	}

	public void setFutureMatchesCount( final int futureMatchesCount ) {
		this.futureMatchesCount = futureMatchesCount;
	}

	public int getFutureMatchesCount() {
		return futureMatchesCount;
	}
}
