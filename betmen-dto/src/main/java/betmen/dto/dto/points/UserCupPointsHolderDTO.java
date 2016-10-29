package betmen.dto.dto.points;

import betmen.dto.dto.UserDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCupPointsHolderDTO {

    private UserDTO user;

    private String matchBetPointsPositive;
    private String matchBetPointsNegative;

    private String matchBetPoints;
    private String matchBonuses;
    private String cupWinnerBonus;

    private String summaryPoints;

    public UserCupPointsHolderDTO(final UserDTO user, final int matchBetPoints, final float matchBonuses, final int cupWinnerBonus) {

        this.user = user;

        this.matchBetPoints = String.format("%d", matchBetPoints);
        this.matchBonuses = String.format("%.2f", matchBonuses);
        this.cupWinnerBonus = String.format("%d", cupWinnerBonus);

        this.summaryPoints = String.format("%.2f", matchBetPoints + matchBonuses + cupWinnerBonus);
    }

    public UserDTO getUser() {
        return user;
    }

    public String getMatchBetPointsPositive() {
        return matchBetPointsPositive;
    }

    public void setMatchBetPointsPositive(final int matchBetPointsPositive) {
        this.matchBetPointsPositive = String.format("%d", matchBetPointsPositive);
    }

    public String getMatchBetPointsNegative() {
        return matchBetPointsNegative;
    }

    public void setMatchBetPointsNegative(final int matchBetPointsNegative) {
        this.matchBetPointsNegative = String.format("%d", matchBetPointsNegative);
    }

    public String getMatchBetPoints() {
        return matchBetPoints;
    }

    public String getMatchBonuses() {
        return matchBonuses;
    }

    public String getCupWinnerBonus() {
        return cupWinnerBonus;
    }

    public String getSummaryPoints() {
        return summaryPoints;
    }

    @Override
    public String toString() {
        return String.format("%s + %s + %s = %s", matchBetPoints, matchBonuses, cupWinnerBonus, summaryPoints);
    }
}
