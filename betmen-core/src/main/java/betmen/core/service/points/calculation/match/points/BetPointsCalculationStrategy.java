package betmen.core.service.points.calculation.match.points;

import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.PointsCalculationStrategy;

public class BetPointsCalculationStrategy {

    private final PointsCalculationStrategy pointsCalculationStrategy;

    public BetPointsCalculationStrategy(final PointsCalculationStrategy strategy) {
        this.pointsCalculationStrategy = strategy;
    }

    static BetPointsCalculationStrategy getInstance(final PointsCalculationStrategy strategy) {
        return new BetPointsCalculationStrategy(strategy);
    }

    final int getPoints(final MatchBet matchBet) {

        if (!matchBet.getMatch().isMatchFinished()) {
            return 0;
        }

        if (userGuessedWinnerAndMatchPoints(matchBet)) {
            return pointsCalculationStrategy.getPointsForMatchScore();
        }

        if (userGuessedWinnerOnly(matchBet)) {
            final int pointsForDelta = getPointsForDelta(matchBet);
            return pointsForDelta > 0 ? pointsForDelta : pointsCalculationStrategy.getPointsForMatchWinner();
        }

        return -pointsCalculationStrategy.getPointsForMatchWinner();
    }

    protected final boolean userGuessedWinnerAndMatchPoints(final MatchBet matchBet) {
        return matchBet.getMatch().getScore1() == matchBet.getBetScore1() && matchBet.getMatch().getScore2() == matchBet.getBetScore2();
    }

    protected final boolean userGuessedWinnerOnly(final MatchBet matchBet) {

        if (hasGuessedWinnerWon(matchBet)) {
            return true;
        }

        return hasMatchFinishedWithGuessedDraw(matchBet);
    }

    protected int getPointsForDelta(final MatchBet matchBet) {
        final Match match = matchBet.getMatch();

        final int score1 = match.getScore1();
        final int score2 = match.getScore2();

        final int betScore1 = matchBet.getBetScore1();
        final int betScore2 = matchBet.getBetScore2();

        final double delta = pointsCalculationStrategy.getPointsDelta();
        final boolean score1WithinDelta = (betScore1 >= score1 - delta) && (betScore1 <= score1 + delta);
        final boolean score2WithinDelta = (betScore2 >= score2 - delta) && (betScore2 <= score2 + delta);

        return score1WithinDelta && score2WithinDelta ? pointsCalculationStrategy.getPointsForBetWithinDelta() : 0;
    }

    private boolean hasGuessedWinnerWon(final MatchBet matchBet) {

        final Match match = matchBet.getMatch();

        final int score1 = match.getScore1();
        final int score2 = match.getScore2();

        final int betScore1 = matchBet.getBetScore1();
        final int betScore2 = matchBet.getBetScore2();

        return (score1 > score2 && betScore1 > betScore2) || (score1 < score2 && betScore1 < betScore2);
    }

    private boolean hasMatchFinishedWithGuessedDraw(final MatchBet matchBet) {

        final Match match = matchBet.getMatch();

        final int score1 = match.getScore1();
        final int score2 = match.getScore2();

        final int betScore1 = matchBet.getBetScore1();
        final int betScore2 = matchBet.getBetScore2();

        final boolean isMatchFinishedWithDraw = score1 == score2;
        final boolean isDrawGuessed = betScore1 == betScore2;

        return isMatchFinishedWithDraw && isDrawGuessed;
    }
}
