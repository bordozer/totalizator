package betmen.core.model.points;

import betmen.core.entity.User;

public class UserSummaryPointsHolder {

    private final User user;

    private final int betPoints;
    private final float matchBonus;

    public UserSummaryPointsHolder(final User user, final int betPoints, final float matchBonus) {

        this.user = user;

        this.betPoints = betPoints;
        this.matchBonus = matchBonus;
    }

    public User getUser() {
        return user;
    }

    public int getBetPoints() {
        return betPoints;
    }

    public float getMatchBonus() {
        return matchBonus;
    }
}
