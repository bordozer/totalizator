package totalizator.app.controllers.ui.teams.standoffs;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Team;

public class TeamsStandoffsModel extends AbstractPageModel {

	private Team team1;
	private Team team2;

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
}
