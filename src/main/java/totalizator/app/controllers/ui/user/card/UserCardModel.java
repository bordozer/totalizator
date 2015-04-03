package totalizator.app.controllers.ui.user.card;

import totalizator.app.models.User;

public class UserCardModel {

	private final User user;
	private String userName;

	public UserCardModel( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
