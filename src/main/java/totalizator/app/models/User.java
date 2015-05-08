package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import static totalizator.app.models.User.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table(name = "users")
@NamedQueries({
		@NamedQuery(
				name = LOAD_ALL,
				query = "select c from User c order by username"
		),
		@NamedQuery(
				name = User.FIND_BY_USERNAME,
				query = "select u from User u where username = :username"
		),
		@NamedQuery(
				name = User.FIND_BY_LOGIN,
				query = "select u from User u where login = :login"
		)
})
public class User extends AbstractEntity {

	public static final String LOAD_ALL = "user.loadAll";
	public static final String FIND_BY_USERNAME = "user.findByUserName";
	public static final java.lang.String FIND_BY_LOGIN = "user.findByUserLogin";

	@Column( unique = true, columnDefinition = "VARCHAR(20)" )
	private String login;

	@Column( unique = true, columnDefinition = "VARCHAR(100)" )
	private String username;

	@Column( columnDefinition = "VARCHAR(255)" )
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
	public int hashCode() {
		return 31 * getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof User ) ) {
			return false;
		}

		final User user = ( User ) obj;
		return user.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s ( %s )", getId(), login, username );
	}
}
