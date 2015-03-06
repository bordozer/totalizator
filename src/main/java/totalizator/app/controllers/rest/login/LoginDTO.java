package totalizator.app.controllers.rest.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class LoginDTO {

	private String login;
	private String password;

//	private Map<String, String> translations;

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

	/*public Map<String, String> getTranslations() {
		return translations;
	}

	public void setTranslations( final Map<String, String> translations ) {
		this.translations = translations;
	}*/
}
