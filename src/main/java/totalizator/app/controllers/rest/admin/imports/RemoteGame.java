package totalizator.app.controllers.rest.admin.imports;

import java.time.LocalDate;

public class RemoteGame {

	private String team1Name;
	private String team2Name;
	private LocalDate gameDate;

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

	public LocalDate getGameDate() {
		return gameDate;
	}

	public void setGameDate( final LocalDate gameDate ) {
		this.gameDate = gameDate;
	}
}
