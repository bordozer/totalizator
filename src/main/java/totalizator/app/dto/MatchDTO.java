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

	private TeamDTO team1;
	private int score1;

	private TeamDTO team2;
	private int score2;

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

	public TeamDTO getTeam1() {
		return team1;
	}

	public void setTeam1( final TeamDTO team1 ) {
		this.team1 = team1;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public TeamDTO getTeam2() {
		return team2;
	}

	public void setTeam2( final TeamDTO team2 ) {
		this.team2 = team2;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
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
		return String.format( "%s vs %s ( %d : %d )", team1, team2, score1, score2 );
	}
}
