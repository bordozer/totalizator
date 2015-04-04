package totalizator.app.controllers.ui.admin;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class AdminModel extends AbstractPageModel {

	protected AdminModel( final User currentUser ) {
		super( currentUser );
	}
}
