package totalizator.app.services.score;

public class PointsCalculationStrategy_Football extends ScoreCalculationStrategy {

	@Override
	int getGuessedWinnerAndMatchPoints() {
		return 3;
	}

	@Override
	int getGuessedWinnerOnlyPoints() {
		return 1;
	}
}
