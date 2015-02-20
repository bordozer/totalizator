package totalizator.app.controllers.rest.user.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UserDTO {

	private String login;
	private String name;

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public void setName( final String name ) {
		this.name = name;
	}
}
