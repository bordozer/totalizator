package totalizator.app.controllers.ui;

import totalizator.app.models.User;

public abstract class AbstractPageModel {

	private final User currentUser;

	protected AbstractPageModel( final User currentUser ) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}
}
