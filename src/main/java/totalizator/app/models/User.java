package totalizator.app.models;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
@NamedQueries({
		@NamedQuery(
				name = User.FIND_BY_USERNAME,
				query = "select u from User u where username = :username"
		)
})
public class User extends AbstractEntity {

	public static final String FIND_BY_USERNAME = "user.findByUserName";

	private String login;
	private String username;
	private String password;

	public User() {
	}

	public User( final String login, final String username, final String password ) {
		this.login = login;
		this.username = username;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin( final String login ) {
		this.login = login;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername( final String username ) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( final String password ) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s ( %s )", getId(), login, username );
	}
}
