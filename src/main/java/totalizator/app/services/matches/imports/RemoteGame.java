package totalizator.app.services.matches.imports;

import java.time.LocalDateTime;

public class RemoteGame {

	private final String remoteGameId;

	private String team1Name;
	private String team2Name;

	private LocalDateTime beginningTime;

	private int score1;
	private int score2;

	private String homeTeamName;
	private boolean finished;

	private int homeTeamNumber;

	public RemoteGame( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}

	public String getRemoteGameId() {
		return remoteGameId;
	}

	public String getTeam1Name() {
		return team1Name;
	}

	public void setTeam1Name( final String team1Name ) {
		this.team1Name = team1Name;
	}

	public String getTeam2Name() {
		return team2Name;
	}

	public void setTeam2Name( final String team2Name ) {
		this.team2Name = team2Name;
	}

	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime( final LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	public int getScore2() {
		return score2;
	}

	public void setHomeTeamName( final String homeTeamName ) {
		this.homeTeamName = homeTeamName;
	}

	public String getHomeTeamName() {
		return homeTeamName;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	@Override
	public String toString() {
		return String.format( "#%s: %s vs %s ( %s )", remoteGameId, team1Name, team2Name, beginningTime );
	}
}
