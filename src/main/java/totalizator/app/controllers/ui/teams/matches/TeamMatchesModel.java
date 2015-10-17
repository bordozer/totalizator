package totalizator.app.controllers.ui.teams.matches;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;

public class TeamMatchesModel extends AbstractPageModel {

	private Cup cup;
	private Team team;

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Cup getCup() {
		return cup;
	}

	public void setTeam( final Team team ) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}
}
