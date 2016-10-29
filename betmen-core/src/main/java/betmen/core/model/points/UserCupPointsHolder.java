package betmen.core.model.points;

import betmen.core.entity.Cup;
import betmen.core.entity.User;

public class UserCupPointsHolder extends AbstractUserPointsHolder {

    private final Cup cup;

    private int matchBetPoints;
    private float matchBonuses;
    private int cupWinnerBonus;

    public UserCupPointsHolder(final User user, final Cup cup) {
        super(user);

        this.cup = cup;
    }

    @Override
    public float getPoints() {
        return (float) matchBetPoints + matchBonuses + (float) cupWinnerBonus;
    }

    public Cup getCup() {
        return cup;
    }

    public int getMatchBetPoints() {
        return matchBetPoints;
    }

    public void setMatchBetPoints(final int matchBetPoints) {
        this.matchBetPoints = matchBetPoints;
    }

    public float getMatchBonuses() {
        return matchBonuses;
    }

    public void setMatchBonuses(final float matchBonuses) {
        this.matchBonuses = matchBonuses;
    }

    public int getCupWinnerBonus() {
        return cupWinnerBonus;
    }

    public void setCupWinnerBonus(final int cupWinnerBonus) {
        this.cupWinnerBonus = cupWinnerBonus;
    }

    @Override
    public String toString() {
        return String.format("%s: %s + %s + %s = %s", cup, matchBetPoints, matchBonuses, cupWinnerBonus, getPoints());
    }
}
