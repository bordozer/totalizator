package totalizator.app.controllers.ui.cups;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.User;

public class CupOverviewModel extends AbstractPageModel {

	private Cup cup;

	protected CupOverviewModel( final User user ) {
		super( user );
	}

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
