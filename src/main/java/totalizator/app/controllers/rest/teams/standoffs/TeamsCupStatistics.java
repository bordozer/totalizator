package totalizator.app.controllers.rest.teams.standoffs;

public class TeamsCupStatistics {

	private int team1won;
	private int team1lost;
	private int team1Total;

	private int team2won;
	private int team2lost;
	private int team2Total;

	public int getTeam1won() {
		return team1won;
	}

	public void setTeam1won( final int team1won ) {
		this.team1won = team1won;
	}

	public int getTeam1lost() {
		return team1lost;
	}

	public void setTeam1lost( final int team1lost ) {
		this.team1lost = team1lost;
	}

	public int getTeam2won() {
		return team2won;
	}

	public void setTeam2won( final int team2won ) {
		this.team2won = team2won;
	}

	public int getTeam2lost() {
		return team2lost;
	}

	public void setTeam2lost( final int team2lost ) {
		this.team2lost = team2lost;
	}

	public int getTeam1Total() {
		return team1Total;
	}

	public void setTeam1Total( final int team1Total ) {
		this.team1Total = team1Total;
	}

	public int getTeam2Total() {
		return team2Total;
	}

	public void setTeam2Total( final int team2Total ) {
		this.team2Total = team2Total;
	}
}
