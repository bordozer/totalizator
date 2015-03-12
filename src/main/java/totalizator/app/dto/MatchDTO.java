package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

	private int team2Id;
	private int score2;

//	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private Date beginningTime;

//	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "CET")
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private Date lastBetTime;

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

	public Date getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime( final Date beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public Date getLastBetTime() {
		return lastBetTime;
	}

	public void setLastBetTime( final Date lastBetTime ) {
		this.lastBetTime = lastBetTime;
	}
}
