package totalizator.app.controllers.ui.cups;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;
import totalizator.app.models.User;

public class CupModel extends AbstractPageModel {

	private Cup cup;

	protected CupModel( final User currentUser ) {
		super( currentUser );
	}

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Cup getCup() {
		return cup;
	}
}
