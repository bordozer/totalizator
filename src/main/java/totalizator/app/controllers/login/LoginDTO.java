package totalizator.app.controllers.login;

public class LoginDTO {

	private String message;

	public LoginDTO() {
	}

	public LoginDTO( final String message ) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( final String message ) {
		this.message = message;
	}
}
