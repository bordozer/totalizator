package totalizator.app.services;

import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;

import java.util.List;

public interface MatchService extends GenericService<Match>{

//	List<Match> loadOpen();

	List<Match> loadAll( final MatchesBetSettingsDTO dto );

	void initModelFromDTO( final MatchDTO matchDTO, final Match match );

	MatchDTO initDTOFromModel( final Match match );
}
