package totalizator.app.dto.points;

import totalizator.app.dto.UserDTO;

public class UserCupPointsHolderDTO {

	private final UserDTO user;

	private float matchBetPointsPositive;
	private float matchBetPointsNegative;

	private final float matchBetPoints;
	private final float matchBonuses;
	private final int cupWinnerBonus;

	private final float summaryPoints;

	public UserCupPointsHolderDTO( final UserDTO user, final float matchBetPoints, final float matchBonuses, final int cupWinnerBonus ) {

		this.user = user;

		this.matchBetPoints = matchBetPoints;
		this.matchBonuses = matchBonuses;
		this.cupWinnerBonus = cupWinnerBonus;

		this.summaryPoints = this.matchBetPoints + this.matchBonuses+ this.cupWinnerBonus;
	}

	public UserDTO getUser() {
		return user;
	}

	public float getMatchBetPointsPositive() {
		return matchBetPointsPositive;
	}

	public void setMatchBetPointsPositive( final float matchBetPointsPositive ) {
		this.matchBetPointsPositive = matchBetPointsPositive;
	}

	public float getMatchBetPointsNegative() {
		return matchBetPointsNegative;
	}

	public void setMatchBetPointsNegative( final float matchBetPointsNegative ) {
		this.matchBetPointsNegative = matchBetPointsNegative;
	}

	public float getMatchBetPoints() {
		return matchBetPoints;
	}

	public float getMatchBonuses() {
		return matchBonuses;
	}

	public int getCupWinnerBonus() {
		return cupWinnerBonus;
	}

	public float getSummaryPoints() {
		return summaryPoints;
	}

	@Override
	public String toString() {
		return String.format( "%s + %s + %s = %s", matchBetPoints, matchBonuses, cupWinnerBonus, summaryPoints );
	}
}
