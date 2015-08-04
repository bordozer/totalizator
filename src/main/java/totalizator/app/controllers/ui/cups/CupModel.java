package totalizator.app.controllers.ui.cups;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Cup;

public class CupModel extends AbstractPageModel {

	private Cup cup;

	public void setCup( final Cup cup ) {
		this.cup = cup;
	}

	public Cup getCup() {
		return cup;
	}
}
