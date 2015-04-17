package totalizator.app.controllers.ui.teams.standoffs;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

public class TeamsStandoffsModel extends AbstractPageModel {

	private Team team1;
	private Team team2;
	private List<Cup> cups;
	private String cupsJSON;

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

	public void setCups( final List<Cup> cups ) {
		this.cups = cups;
	}

	public List<Cup> getCups() {
		return cups;
	}

	public void setCupsJSON( final String cupsJSON ) {
		this.cupsJSON = cupsJSON;
	}

	public String getCupsJSON() {
		return cupsJSON;
	}
}
