package totalizator.app.controllers.rest.matches.bets;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateSerializer;

import java.time.LocalDate;
import java.util.List;

public class MatchBetsOnDateDTO {

	private final LocalDate date;
	private final List<Integer> matchIds;

	public MatchBetsOnDateDTO( final LocalDate date, final List<Integer> matchIds ) {
		this.date = date;
		this.matchIds = matchIds;
	}

	@JsonSerialize( using = DateSerializer.class )
	public LocalDate getDate() {
		return date;
	}

	public List<Integer> getMatchIds() {
		return matchIds;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d", date, matchIds.size() );
	}
}
