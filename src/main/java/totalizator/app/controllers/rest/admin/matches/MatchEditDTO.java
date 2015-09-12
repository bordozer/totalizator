package totalizator.app.controllers.rest.admin.matches;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class MatchEditDTO {

	private int matchId;

	private int categoryId;

	private int cupId;

	private int team1Id;
	private int score1;

	private int team2Id;
	private int score2;

	private LocalDateTime beginningTime;
	private boolean matchFinished;

	private int homeTeamNumber;
	private String matchDescription;

	private int betsCount;

	private String remoteGameId;

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getTeam1Id() {
		return team1Id;
	}

	public void setTeam1Id( final int team1Id ) {
		this.team1Id = team1Id;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getTeam2Id() {
		return team2Id;
	}

	public void setTeam2Id( final int team2Id ) {
		this.team2Id = team2Id;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setBeginningTime( final LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public boolean isMatchFinished() {
		return matchFinished;
	}

	public void setMatchFinished( final boolean matchFinished ) {
		this.matchFinished = matchFinished;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	public String getMatchDescription() {
		return matchDescription;
	}

	public void setMatchDescription( final String matchDescription ) {
		this.matchDescription = matchDescription;
	}

	public int getBetsCount() {
		return betsCount;
	}

	public void setBetsCount( final int betsCount ) {
		this.betsCount = betsCount;
	}

	public String getRemoteGameId() {
		return remoteGameId;
	}

	public void setRemoteGameId( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}
}
