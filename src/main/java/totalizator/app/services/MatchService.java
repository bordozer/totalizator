package totalizator.app.services;

import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchService extends GenericService<Match>{

	List<Match> loadAll( final Cup cup );

	List<Match> loadAll( final MatchesBetSettingsDTO dto );

	boolean isMatchStarted( Match match );

	boolean isMatchFinished( Match match );

	Match find( final Team team1, final Team team2, final LocalDateTime localDateTime );

	List<Match> find( final Team team1, final Team team2 );
}
