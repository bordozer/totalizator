package totalizator.app.dto;

import org.apache.commons.lang.StringEscapeUtils;
import totalizator.app.models.User;

public class UserDTO {

	private int userId;
	private String userName;

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

	@Override
	public int hashCode() {
		return 31 * getUserId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj.getClass().equals( this.getClass() ) ) ) {
			return false;
		}

		final UserDTO user = ( UserDTO ) obj;
		return user.getUserId() == getUserId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", userId, userName );
	}
}
