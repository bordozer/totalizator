package totalizator.app.dto;

import org.apache.commons.lang.StringEscapeUtils;
import totalizator.app.beans.UserTitle;
import totalizator.app.models.User;

public class UserDTO {

	private int userId;
	private String userName;
	private UserTitle userTitle;

	public UserDTO( final User user ) {
		this.userId = user.getId();
		this.userName = StringEscapeUtils.escapeJavaScript( user.getUsername() );
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

	public UserTitle getUserTitle() {
		return userTitle;
	}

	public void setUserTitle( final UserTitle userTitle ) {
		this.userTitle = userTitle;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", userId, userName );
	}
}
