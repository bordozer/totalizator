package totalizator.app.controllers.ui.teams.card;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Team;

public class TeamCardModel extends AbstractPageModel {

	private Team team;

	public Team getTeam() {

		return team;
	}
	public void setTeam( final Team team ) {

		this.team = team;
	}
}
