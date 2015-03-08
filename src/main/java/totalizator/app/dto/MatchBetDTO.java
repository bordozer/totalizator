package totalizator.app.dto;

import java.util.List;

public class MatchBetDTO {

	private int matchId;

	private MatchDTO match;
	private List<BetDTO> bets;

	public MatchBetDTO() {
	}

	public MatchBetDTO( final MatchDTO match ) {
		this.matchId = match.getMatchId();
		this.match = match;
	}

	public MatchBetDTO( final MatchDTO match, final List<BetDTO> bets ) {
		this.matchId = match.getMatchId();
		this.match = match;
		this.bets = bets;
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

	public List<BetDTO> getBets() {
		return bets;
	}

	public void setBets( final List<BetDTO> bets ) {
		this.bets = bets;
	}

	@Override
	public String toString() {
		return String.format( "%s %s", match, bets );
	}
}
