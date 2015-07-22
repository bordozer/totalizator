package totalizator.app.services.score;

import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;

abstract class ScoreCalculationStrategy {

	public static final ScoreCalculationStrategy BASKETBALL_SCORE_CALCULATION_STRATEGY = new PointsCalculationStrategy_Basketball();
	public static final ScoreCalculationStrategy FOOTBALL_SCORE_CALCULATION_STRATEGY = new PointsCalculationStrategy_Football();

	abstract int getGuessedWinnerAndMatchPoints();

	abstract int getGuessedWinnerOnlyPoints();

	static ScoreCalculationStrategy getInstance( final PointsCalculationStrategyType strategy ) {

		switch ( strategy ) {
			case BASKETBALL:
				return BASKETBALL_SCORE_CALCULATION_STRATEGY;
			case FOOTBALL:
				return FOOTBALL_SCORE_CALCULATION_STRATEGY;
		}

		throw new IllegalArgumentException( String.format( "Unsupported Points Calculation Strategy Type: %s", strategy ) );
	}

	final int getPoints( final MatchBet matchBet ) {

		if ( ! matchBet.getMatch().isMatchFinished() ) {
			return 0;
		}

		if ( userGuessedWinnerAndMatchPoints( matchBet ) ) {
			return getGuessedWinnerAndMatchPoints();
		}

		if ( userGuessedWinnerOnly( matchBet ) ) {
			final int categoryCustomPoints = getCategoryCustomPoints( matchBet );
			return categoryCustomPoints > 0 ? categoryCustomPoints : getGuessedWinnerOnlyPoints();
		}

		return 0;
	}

	protected final boolean userGuessedWinnerAndMatchPoints( final MatchBet matchBet ) {
		return matchBet.getMatch().getScore1() == matchBet.getBetScore1() && matchBet.getMatch().getScore2() == matchBet.getBetScore2();
	}

	protected final boolean userGuessedWinnerOnly( final MatchBet matchBet ) {

		if ( hasGuessedWinnerWon( matchBet ) ) {
			return true;
		}

		return hasMatchFinishedWithGuessedDraw( matchBet );
	}

	protected int getCategoryCustomPoints( final MatchBet matchBet ) {
		return 0;
	}

	private boolean hasGuessedWinnerWon( final MatchBet matchBet ) {

		final Match match = matchBet.getMatch();

		final int score1 = match.getScore1();
		final int score2 = match.getScore2();

		final int betScore1 = matchBet.getBetScore1();
		final int betScore2 = matchBet.getBetScore2();

		return ( score1 > score2 && betScore1 > betScore2 ) || ( score1 < score2 && betScore1 < betScore2 );
	}

	private boolean hasMatchFinishedWithGuessedDraw( final MatchBet matchBet ) {

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
