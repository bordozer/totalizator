package totalizator.app.controllers.ui.user.card;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class UserCardModel extends AbstractPageModel {

	private final User user;

	public UserCardModel( final User user, final User currentUser ) {
		super( currentUser );
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}