package totalizator.app.dto;

public class MatchBetDTO {

	private int matchId;

	private MatchDTO match;
	private BetDTO bet;

	public MatchBetDTO() {
	}

	public MatchBetDTO( final MatchDTO match ) {
		this.matchId = match.getMatchId();
		this.match = match;
	}

	public MatchBetDTO( final MatchDTO match, final BetDTO bet ) {
		this.matchId = match.getMatchId();
		this.match = match;
		this.bet = bet;
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

	@Override
	public String toString() {
		return String.format( "%s %s", match, bet );
	}
}
