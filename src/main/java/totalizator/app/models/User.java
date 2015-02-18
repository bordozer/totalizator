package totalizator.app.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class User extends AbstractEntity {

	private String login;
	private String name;
	private String password;

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

	public String getPassword() {
		return password;
	}

	public void setPassword( final String password ) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s ( %s )", getId(), login, name );
	}
}
