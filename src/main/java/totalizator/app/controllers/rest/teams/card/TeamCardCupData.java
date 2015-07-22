package totalizator.app.controllers.rest.teams.card;

import totalizator.app.dto.CupDTO;
import totalizator.app.dto.CupWinnerDTO;

public class TeamCardCupData {

	private CupDTO cup;

	private CupWinnerDTO cupWinner;

	private int matchCount;
	private int wonMatchCount;

	public TeamCardCupData( final CupDTO cup ) {
		this.cup = cup;
	}

	public CupDTO getCup() {
		return cup;
	}

	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}

	public CupWinnerDTO getCupWinner() {
		return cupWinner;
	}

	public void setCupWinner( final CupWinnerDTO cupWinner ) {
		this.cupWinner = cupWinner;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount( final int matchCount ) {
		this.matchCount = matchCount;
	}

	public int getWonMatchCount() {
		return wonMatchCount;
	}

	public void setWonMatchCount( final int wonMatchCount ) {
		this.wonMatchCount = wonMatchCount;
	}
}
