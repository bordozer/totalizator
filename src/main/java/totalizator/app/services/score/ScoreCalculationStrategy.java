package totalizator.app.services.score;

import totalizator.app.models.MatchBet;

abstract class ScoreCalculationStrategy {

	abstract int getPoints( final MatchBet bet );

	static ScoreCalculationStrategy getInstance() {
		return new BaseScoreCalculationStrategy();
	}
}
