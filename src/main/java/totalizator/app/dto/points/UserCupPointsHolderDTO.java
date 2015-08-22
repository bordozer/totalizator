package totalizator.app.dto.points;

import totalizator.app.dto.UserDTO;

public class UserCupPointsHolderDTO {

	private final UserDTO user;

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
