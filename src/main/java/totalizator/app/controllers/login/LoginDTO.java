package totalizator.app.controllers.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class LoginDTO {

	private String login;
	private String password;

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( final String password ) {
		this.password = password;
	}
}
