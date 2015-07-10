package totalizator.app.controllers.ui.cups.matches;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.User;

public class CupMatchesModel extends AbstractPageModel {

	private Cup cup;

	protected CupMatchesModel( final User currentUser ) {
		super( currentUser );
	}

	public Cup getCup() {
		return cup;
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}
}
