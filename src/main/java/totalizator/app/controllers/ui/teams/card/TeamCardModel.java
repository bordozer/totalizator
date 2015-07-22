package totalizator.app.controllers.ui.teams.card;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public class TeamCardModel extends AbstractPageModel {

	private Team team;

	protected TeamCardModel( final User currentUser ) {
		super( currentUser );
	}

	public Team getTeam() {

		return team;
	}
	public void setTeam( final Team team ) {

		this.team = team;
	}
}
