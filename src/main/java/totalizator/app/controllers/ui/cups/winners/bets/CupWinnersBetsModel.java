package totalizator.app.controllers.ui.cups.winners.bets;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.User;

public class CupWinnersBetsModel extends AbstractPageModel {

	private Cup cup;

	protected CupWinnersBetsModel( final User currentUser ) {
		super( currentUser );
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Cup getCup() {
		return cup;
	}
}
