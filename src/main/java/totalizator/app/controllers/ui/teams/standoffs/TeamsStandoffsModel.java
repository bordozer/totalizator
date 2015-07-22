package totalizator.app.controllers.ui.teams.standoffs;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public class TeamsStandoffsModel extends AbstractPageModel {

	private Team team1;
	private Team team2;
	private String cups;
	private String team1JSON;
	private String team2JSON;
	private int score1;
	private int score2;

	protected TeamsStandoffsModel( final User currentUser ) {
		super( currentUser );
	}

	public void setTeam1( final Team team1 ) {
		this.team1 = team1;
	}

	public Team getTeam1() {
		return team1;
	}

	public void setTeam2( final Team team2 ) {
		this.team2 = team2;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setCups( final String cups ) {
		this.cups = cups;
	}

	public String getCups() {
		return cups;
	}

	public void setTeam1JSON( final String team1JSON ) {
		this.team1JSON = team1JSON;
	}

	public String getTeam1JSON() {
		return team1JSON;
	}

	public void setTeam2JSON( final String team2JSON ) {
		this.team2JSON = team2JSON;
	}

	public String getTeam2JSON() {
		return team2JSON;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}
}
