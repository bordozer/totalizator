package totalizator.app.controllers.ui.cups.matches;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public class CupMatchesModel extends AbstractPageModel {

	private Cup cup;
	private Team team1;
	private Team team2;

	protected CupMatchesModel( final User currentUser ) {
		super( currentUser );
	}

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
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
}
