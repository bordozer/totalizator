package totalizator.app.services;

import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;

import java.util.List;

public interface MatchService extends GenericService<Match>{

	List<Match> loadOpen();

	void initModelFromDTO( MatchDTO matchDTO, Match match );

	MatchDTO initDTOFromModel( Match match );
}
