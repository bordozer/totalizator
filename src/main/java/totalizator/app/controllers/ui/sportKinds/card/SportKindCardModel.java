package totalizator.app.controllers.ui.sportKinds.card;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.SportKind;

public class SportKindCardModel extends AbstractPageModel {

	private final SportKind sportKind;

	public SportKindCardModel( final SportKind sportKind ) {
		this.sportKind = sportKind;
	}

	public SportKind getSportKind() {
		return sportKind;
	}
}
