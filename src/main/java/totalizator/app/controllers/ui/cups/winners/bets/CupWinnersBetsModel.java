package totalizator.app.controllers.ui.cups.winners.bets;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;

public class CupWinnersBetsModel extends AbstractPageModel {

	private Cup cup;

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Cup getCup() {
		return cup;
	}
}
