package totalizator.app.dto;

import totalizator.app.dto.points.UserMatchPointsHolderDTO;

public class MatchBetDTO {

	private int matchId;

	private MatchDTO match;
	private BetDTO bet;
	private UserMatchPointsHolderDTO userMatchPointsHolder;

	private boolean bettingAllowed;
	private String bettingValidationMessage;

	private int totalBets;

	public MatchBetDTO() {
	}

	public MatchBetDTO( final MatchDTO match ) {
		this.matchId = match.getMatchId();
		this.match = match;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public MatchDTO getMatch() {
		return match;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}

	public BetDTO getBet() {
		return bet;
	}

	public void setBet( final BetDTO bet ) {
		this.bet = bet;
	}

	public boolean isBettingAllowed() {
		return bettingAllowed;
	}

	public void setBettingAllowed( final boolean bettingAllowed ) {
		this.bettingAllowed = bettingAllowed;
	}

	public String getBettingValidationMessage() {
		return bettingValidationMessage;
	}

	public void setBettingValidationMessage( final String bettingValidationMessage ) {
		this.bettingValidationMessage = bettingValidationMessage;
	}

	public UserMatchPointsHolderDTO getUserMatchPointsHolder() {
		return userMatchPointsHolder;
	}

	public void setUserMatchPointsHolder( final UserMatchPointsHolderDTO userMatchPointsHolder ) {
		this.userMatchPointsHolder = userMatchPointsHolder;
	}

	public int getTotalBets() {
		return totalBets;
	}

	public void setTotalBets(final int totalBets) {
		this.totalBets = totalBets;
	}

	@Override
	public String toString() {
		return String.format( "%s %s", match, bet );
	}
}
