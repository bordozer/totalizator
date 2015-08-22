package totalizator.app.beans.points;

import totalizator.app.models.User;

public abstract class AbstractUserPointsHolder {

	protected final User user;

	public abstract float getPoints();

	public AbstractUserPointsHolder( final User user ) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
