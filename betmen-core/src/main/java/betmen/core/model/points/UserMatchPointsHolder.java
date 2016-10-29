package betmen.core.model.points;

public class UserMatchPointsHolder extends AbstractUserPointsHolder {

    private final UserMatchBetPointsHolder userMatchBetPointsHolder;
    private float matchBonus;

    public UserMatchPointsHolder(final UserMatchBetPointsHolder userMatchBetPointsHolder) {
        super(userMatchBetPointsHolder.getMatchBet().getUser());

        this.userMatchBetPointsHolder = userMatchBetPointsHolder;
    }

    public UserMatchPointsHolder(final UserMatchBetPointsHolder userMatchBetPointsHolder, final float matchBonus) {
        super(userMatchBetPointsHolder.getMatchBet().getUser());

        this.userMatchBetPointsHolder = userMatchBetPointsHolder;
        this.matchBonus = matchBonus;
    }

    @Override
    public float getPoints() {
        return userMatchBetPointsHolder.getPoints() + matchBonus;
    }

    public UserMatchBetPointsHolder getUserMatchBetPointsHolder() {
        return userMatchBetPointsHolder;
    }

    public float getUserBetPoints() {
        return userMatchBetPointsHolder.getPoints();
    }

    public int getMatchBetPoints() {
        return userMatchBetPointsHolder.getMatchBetPoints();
    }

    public float getMatchBonus() {
        return matchBonus;
    }

    public void setMatchBonus(final float matchBonus) {
        this.matchBonus = matchBonus;
    }

    @Override
    public String toString() {
        return String.format("%s ( + %s )", userMatchBetPointsHolder, matchBonus);
    }
}
