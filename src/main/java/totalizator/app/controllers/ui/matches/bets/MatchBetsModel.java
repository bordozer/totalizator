package totalizator.app.controllers.ui.matches.bets;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Match;
import totalizator.app.models.User;

public class MatchBetsModel extends AbstractPageModel {

	private Match match;
	private String matchTime;

	protected MatchBetsModel( final User currentUser ) {
		super( currentUser );
	}

	public void setMatch( final Match match ) {
		this.match = match;
	}

	public Match getMatch() {
		return match;
	}

	public void setMatchTime( final String matchTime ) {
		this.matchTime = matchTime;
	}

	public String getMatchTime() {
		return matchTime;
	}
}
