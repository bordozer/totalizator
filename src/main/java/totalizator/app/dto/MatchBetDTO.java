package totalizator.app.dto;

public class MatchBetDTO {

	private int matchId;

	private MatchDTO match;
	private BetDTO bet;
	private boolean bettingAllowed;
	private int points;

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

	public int getPoints() {
		return points;
	}

	public void setPoints( final int points ) {
		this.points = points;
	}

	@Override
	public String toString() {
		return String.format( "%s %s", match, bet );
	}
}
