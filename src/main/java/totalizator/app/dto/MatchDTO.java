package totalizator.app.dto;

import java.util.Date;

public class MatchDTO {

	private int matchId;

	private int categoryId;

	private int cupId;

	private int team1Id;
	private int score1Id;

	private int team2Id;
	private int score2Id;

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

	public int getScore1Id() {
		return score1Id;
	}

	public void setScore1Id( final int score1Id ) {
		this.score1Id = score1Id;
	}

	public int getTeam2Id() {
		return team2Id;
	}

	public void setTeam2Id( final int team2Id ) {
		this.team2Id = team2Id;
	}

	public int getScore2Id() {
		return score2Id;
	}

	public void setScore2Id( final int score2Id ) {
		this.score2Id = score2Id;
	}

	public Date getLastBetTime() {
		return lastBetTime;
	}

	public void setLastBetTime( final Date lastBetTime ) {
		this.lastBetTime = lastBetTime;
	}
}
