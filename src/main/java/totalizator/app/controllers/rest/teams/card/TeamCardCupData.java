package totalizator.app.controllers.rest.teams.card;

import totalizator.app.dto.CupDTO;
import totalizator.app.dto.CupWinnerDTO;

public class TeamCardCupData {

	private CupDTO cup;

	private CupWinnerDTO cupWinner;

	private int finishedMatchCount;
	private int wonMatchCount;
	private int futureMatchesCount;

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

	public int getFinishedMatchCount() {
		return finishedMatchCount;
	}

	public void setFinishedMatchCount( int finishedMatchCount ) {
		this.finishedMatchCount = finishedMatchCount;
	}

	public int getWonMatchCount() {
		return wonMatchCount;
	}

	public void setWonMatchCount( final int wonMatchCount ) {
		this.wonMatchCount = wonMatchCount;
	}

	public int getFutureMatchesCount() {
		return futureMatchesCount;
	}

	public void setFutureMatchesCount( int futureMatchesCount ) {
		this.futureMatchesCount = futureMatchesCount;
	}
}
