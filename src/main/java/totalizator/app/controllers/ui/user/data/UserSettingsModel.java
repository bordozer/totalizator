package totalizator.app.controllers.ui.user.data;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class UserSettingsModel extends AbstractPageModel {

	private User user;

	protected UserSettingsModel( final User currentUser ) {
		super( currentUser );
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}