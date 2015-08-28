package totalizator.app.controllers.rest.matches.bets;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.serialization.DateSerializer;

import java.time.LocalDate;
import java.util.List;

public class MatchBetsOnDateDTO {

	private final LocalDate date;
	private final List<MatchBetDTO> matchBets;

	public MatchBetsOnDateDTO( final LocalDate date, final List<MatchBetDTO> matchBets ) {
		this.date = date;
		this.matchBets = matchBets;
	}

	@JsonSerialize( using = DateSerializer.class )
	public LocalDate getDate() {
		return date;
	}

	public List<MatchBetDTO> getMatchBets() {
		return matchBets;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d", date, matchBets.size() );
	}
}
