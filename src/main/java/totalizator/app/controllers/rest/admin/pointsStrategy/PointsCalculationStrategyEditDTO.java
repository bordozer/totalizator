package totalizator.app.controllers.rest.admin.pointsStrategy;

public class PointsCalculationStrategyEditDTO {

	private int pcsId;
	private String strategyName;

	private int pointsForMatchScore;
	private int pointsForMatchWinner;

	private int pointsDelta;
	private int pointsForBetWithinDelta;

	public int getPcsId() {
		return pcsId;
	}

	public void setPcsId( final int pcsId ) {
		this.pcsId = pcsId;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName( final String strategyName ) {
		this.strategyName = strategyName;
	}

	public int getPointsForMatchScore() {
		return pointsForMatchScore;
	}

	public void setPointsForMatchScore( final int pointsForMatchScore ) {
		this.pointsForMatchScore = pointsForMatchScore;
	}

	public int getPointsForMatchWinner() {
		return pointsForMatchWinner;
	}

	public void setPointsForMatchWinner( final int pointsForMatchWinner ) {
		this.pointsForMatchWinner = pointsForMatchWinner;
	}

	public int getPointsDelta() {
		return pointsDelta;
	}

	public void setPointsDelta( final int pointsDelta ) {
		this.pointsDelta = pointsDelta;
	}

	public int getPointsForBetWithinDelta() {
		return pointsForBetWithinDelta;
	}

	public void setPointsForBetWithinDelta( final int pointsForBetWithinDelta ) {
		this.pointsForBetWithinDelta = pointsForBetWithinDelta;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", pcsId, strategyName );
	}
}
