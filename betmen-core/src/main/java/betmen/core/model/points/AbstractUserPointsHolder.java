package betmen.core.model.points;

import betmen.core.entity.User;

public abstract class AbstractUserPointsHolder {

    protected final User user;

    public abstract float getPoints();

    public AbstractUserPointsHolder(final User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
