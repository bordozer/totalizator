package totalizator.app.services;

import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;

import java.util.List;

public interface MatchService extends GenericService<Match>{

	List<Match> loadAll( final Cup cup );

	List<Match> loadAll( final MatchesBetSettingsDTO dto );

	void initModelFromDTO( final MatchDTO matchDTO, final Match match );
}
