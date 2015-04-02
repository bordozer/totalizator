package totalizator.app.beans;

import totalizator.app.models.User;

public class UserPoints {

	private final User user;
	private int points;

	public UserPoints( final User user, final int points ) {
		this.user = user;
		this.points = points;
	}

	public User getUser() {
		return user;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints( final int points ) {
		this.points = points;
	}
}
