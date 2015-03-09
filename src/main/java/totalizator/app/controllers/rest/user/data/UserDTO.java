package totalizator.app.controllers.rest.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserDTO {

	private int userId;
	private String login;
	private String userName;

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}
}
