package totalizator.app.controllers.ui.teams.standoffs;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class TeamsStandoffsModel extends AbstractPageModel {

	private String matchesAndBetsJSON;

	protected TeamsStandoffsModel( final User currentUser ) {
		super( currentUser );
	}

	public void setMatchesAndBetsJSON( final String matchesAndBetsJSON ) {
		this.matchesAndBetsJSON = matchesAndBetsJSON;
	}

	public String getMatchesAndBetsJSON() {
		return matchesAndBetsJSON;
	}
}
