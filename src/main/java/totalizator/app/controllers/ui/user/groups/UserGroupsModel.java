package totalizator.app.controllers.ui.user.groups;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class UserGroupsModel extends AbstractPageModel {

	private User user;

	protected UserGroupsModel( final User currentUser ) {
		super( currentUser );
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}
}