package totalizator.app.services.score;

import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;

class PointsCalculationStrategy_Basketball extends ScoreCalculationStrategy {

	private static final int MATCH_POINTS_GUESSING_DELTA = 3;
	private static final int CUSTOM_POINTS = 3;

	@Override
	int getGuessedWinnerAndMatchPoints() {
		return 6;
	}

	@Override
	int getGuessedWinnerOnlyPoints() {
		return 1;
	}

	@Override
	protected int getCategoryCustomPoints( final MatchBet matchBet ) {

		final Match match = matchBet.getMatch();

		final int score1 = match.getScore1();
		final int score2 = match.getScore2();

		final int betScore1 = matchBet.getBetScore1();
		final int betScore2 = matchBet.getBetScore2();

		final boolean score1WithinDelta = ( betScore1 >= score1 - MATCH_POINTS_GUESSING_DELTA ) && ( betScore1 <= score1 + MATCH_POINTS_GUESSING_DELTA );
		final boolean score2WithinDelta = ( betScore2 >= score2 - MATCH_POINTS_GUESSING_DELTA ) && ( betScore2 <= score2 + MATCH_POINTS_GUESSING_DELTA );

		return score1WithinDelta && score2WithinDelta ? CUSTOM_POINTS : 0;
	}
}
