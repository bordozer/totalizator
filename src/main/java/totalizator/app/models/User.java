package totalizator.app.models;

public class User {

	private int id;
	private String login;
	private String name;
	private String password;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

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
		return String.format( "#%d: %s ( %s )", id, login, name );
	}
}
