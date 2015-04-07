package totalizator.app.dto;

import totalizator.app.models.User;

public class UserDTO {

	private int userId;
	private String userName;

	public UserDTO( final User user ) {
		this.userId = user.getId();
		this.userName = user.getUsername();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", userId, userName );
	}
}
