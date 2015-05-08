package totalizator.app.dao;

import totalizator.app.models.CupTeam;
import totalizator.app.services.GenericService;

import java.util.List;

public interface CupTeamDao extends GenericService<CupTeam> {

	List<CupTeam> loadAll( final int cupId );

	CupTeam load( final int cupId, final int teamId );
}
