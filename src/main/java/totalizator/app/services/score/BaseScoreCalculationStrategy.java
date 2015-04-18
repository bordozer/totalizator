package totalizator.app.services.score;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;

class BaseScoreCalculationStrategy extends ScoreCalculationStrategy {

	private static final int MATCH_POINTS_GUESSING_DELTA = 3;

	@Override
	int getPoints( final MatchBet bet ) {

		if ( ! bet.getMatch().isMatchFinished() ) {
			return 0;
		}

		final Cup cup = bet.getMatch().getCup();

		final Match match = bet.getMatch();

		final int score1 = match.getScore1();
		final int score2 = match.getScore2();

		final int betScore1 = bet.getBetScore1();
		final int betScore2 = bet.getBetScore2();

		if ( isMatchPointsGuessedRight( betScore1, betScore2, score1, score2 ) ) {
			return cup.getGuessedRightPoints();
		}

		if ( isMatchWinnerGuessedRight( betScore1, betScore2, score1, score2 ) ) {
			return cup.getGuessedRightWinners();
		}

		if ( isPointsWithingGuessed( betScore1, betScore2, score1, score2 ) ) {
			return cup.getGuessedPointsWithinDeltaPoints();
		}

		return 0;
	}

	private boolean isMatchPointsGuessedRight( final int betScore1, final int betScore2, final int score1, final int score2 ) {
		return score1 == betScore1 && score2 == betScore2;
	}

	private boolean isMatchWinnerGuessedRight( final int betScore1, final int betScore2, final int score1, final int score2 ) {

		if ( hasGuessedWinnerWon( betScore1, betScore2, score1, score2 ) ) {
			return true;
		}

		return hasMatchFinishedWithGuessedDraw( betScore1, betScore2, score1, score2 );
	}

	private boolean hasGuessedWinnerWon( final int betScore1, final int betScore2, final int score1, final int score2 ) {
		return ( score1 > score2 && betScore1 > betScore2 ) || ( score1 < score2 && betScore1 < betScore2 );
	}

	private boolean hasMatchFinishedWithGuessedDraw( final int betScore1, final int betScore2, final int score1, final int score2 ) {

		final boolean isMatchFinishedWithDraw = score1 == score2;
		final boolean isDrawGuessed = betScore1 == betScore2;

		return isMatchFinishedWithDraw && isDrawGuessed;
	}

	private boolean isPointsWithingGuessed( final int betScore1, final int betScore2, final int score1, final int score2 ) {

		final boolean score1WithinDelta = ( betScore1 >= score1 - MATCH_POINTS_GUESSING_DELTA ) || ( betScore1 <= score1 + MATCH_POINTS_GUESSING_DELTA );
		final boolean score2WithinDelta = ( betScore2 >= score2 - MATCH_POINTS_GUESSING_DELTA ) || ( betScore2 <= score2 + MATCH_POINTS_GUESSING_DELTA );

		return score1WithinDelta && score2WithinDelta;
	}
}
