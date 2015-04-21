package totalizator.app.services.score;

import totalizator.app.models.MatchBet;

abstract class ScoreCalculationStrategy {

	public static final BaseScoreCalculationStrategy BASE_SCORE_CALCULATION_STRATEGY = new BaseScoreCalculationStrategy();

	abstract int getPoints( final MatchBet bet );

	static ScoreCalculationStrategy getInstance() {
		return BASE_SCORE_CALCULATION_STRATEGY;
	}
}
