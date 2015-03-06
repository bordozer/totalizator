package totalizator.app.dto;

import java.util.Date;

public class MatchDTO {

	private int matchId;

	private String categoryName;

	private int cupId;
	private String cupName;

	private int team1Id;
	private String team1Name;
	private int score1Id;

	private int team2Id;
	private String team2Name;
	private int score2Id;

	private Date lastBetTime;

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName( final String categoryName ) {
		this.categoryName = categoryName;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	public int getTeam1Id() {
		return team1Id;
	}

	public void setTeam1Id( final int team1Id ) {
		this.team1Id = team1Id;
	}

	public String getTeam1Name() {
		return team1Name;
	}

	public void setTeam1Name( final String team1Name ) {
		this.team1Name = team1Name;
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

	public String getTeam2Name() {
		return team2Name;
	}

	public void setTeam2Name( final String team2Name ) {
		this.team2Name = team2Name;
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
