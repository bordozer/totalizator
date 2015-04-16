package totalizator.app.controllers.rest.admin.imports;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RemoteGame {

	private String team1Name;
	private String team2Name;
	private LocalDateTime gameDate;
	private int score1;
	private int score2;
	private String homeTeamName;
	private boolean finished;

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

	public LocalDateTime getGameDate() {
		return gameDate;
	}

	public void setGameDate( final LocalDateTime gameDate ) {
		this.gameDate = gameDate;
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
}
