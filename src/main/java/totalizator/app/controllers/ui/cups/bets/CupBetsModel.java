package totalizator.app.controllers.ui.cups.bets;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;

public class CupBetsModel extends AbstractPageModel {

	private Cup cup;

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	@Override
	public String toString() {
		return String.format( "%s", cup );
	}
}
