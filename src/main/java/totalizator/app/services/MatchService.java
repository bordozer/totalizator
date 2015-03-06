package totalizator.app.services;

import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;

public interface MatchService extends GenericService<Match>{

	void initModelFromDTO( MatchDTO matchDTO, Match match );

	MatchDTO initDTOFromModel( Match match );
}
