package totalizator.app.services.score;

import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;

class BaseScoreCalculationStrategy extends ScoreCalculationStrategy {

	private static final int GUESSED_RIGHT_MATCH_RESULT_POINTS_POINTS = 3;
	private static final int GUESSED_RIGHT_MATCH_WINNER_OR_DRAW_POINTS = 1;

	@Override
	int getPoints( final MatchBet bet ) {

		final Match match = bet.getMatch();

		final int score1 = match.getScore1();
		final int score2 = match.getScore2();

		final int betScore1 = bet.getBetScore1();
		final int betScore2 = bet.getBetScore2();

		if ( isMatchPointsGuessedRight( betScore1, betScore2, score1, score2 ) ) {
			return GUESSED_RIGHT_MATCH_RESULT_POINTS_POINTS;
		}

		if ( isMatchWinnerGuessedRight( betScore1, betScore2, score1, score2 ) ) {
			return GUESSED_RIGHT_MATCH_WINNER_OR_DRAW_POINTS;
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
}
