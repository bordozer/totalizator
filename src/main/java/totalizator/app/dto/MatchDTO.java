package totalizator.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.util.Date;

public class MatchDTO {

	private int matchId;

	private int categoryId;

	private int cupId;

	private int team1Id;
	private int score1;
	private String team1Logo;

	private int team2Id;
	private int score2;
	private String team2Logo;

	private Date beginningTime;
	private boolean matchFinished;

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

	public String getTeam1Logo() {
		return team1Logo;
	}

	public void setTeam1Logo( final String team1Logo ) {
		this.team1Logo = team1Logo;
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

	public String getTeam2Logo() {
		return team2Logo;
	}

	public void setTeam2Logo( final String team2Logo ) {
		this.team2Logo = team2Logo;
	}

	@JsonSerialize(using = DateTimeSerializer.class)
	public Date getBeginningTime() {
		return beginningTime;
	}

	@JsonDeserialize(using = DateTimeDeserializer.class)
	public void setBeginningTime( final Date beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public boolean isMatchFinished() {
		return matchFinished;
	}

	public void setMatchFinished( final boolean matchFinished ) {
		this.matchFinished = matchFinished;
	}

	@Override
	public String toString() {
		return String.format( "%d vs %d ( %d : %d )", team1Id, team2Id, score1, score2 );
	}
}
