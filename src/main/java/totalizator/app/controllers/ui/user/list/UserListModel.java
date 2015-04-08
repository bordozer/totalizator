package totalizator.app.controllers.ui.user.list;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class UserListModel extends AbstractPageModel {

	protected UserListModel( final User currentUser ) {
		super( currentUser );
	}
}
