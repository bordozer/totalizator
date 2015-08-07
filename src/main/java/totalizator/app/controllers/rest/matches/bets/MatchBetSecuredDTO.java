package totalizator.app.controllers.rest.matches.bets;

import totalizator.app.dto.MatchBetDTO;

public class MatchBetSecuredDTO {

	private final MatchBetDTO matchBet;
	private final boolean hiddenBet;

	public MatchBetSecuredDTO( final MatchBetDTO matchBet, final boolean hiddenBet ) {
		this.matchBet = matchBet;
		this.hiddenBet = hiddenBet;
	}

	public MatchBetDTO getMatchBet() {
		return matchBet;
	}

	public boolean isHiddenBet() {
		return hiddenBet;
	}
}
