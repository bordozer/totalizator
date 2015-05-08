package totalizator.app.dao;

import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.util.List;

public interface CupWinnerDao extends GenericService<CupWinner> {

	String CACHE_ENTRY = "totalizator.app.cache.cup-winner";
	String CACHE_QUERY = "totalizator.app.cache.cup-winner.query";

	List<CupWinner> loadAll( final Cup cup );

	List<CupWinner> loadAll( final Cup cup, final Team team );

	void saveAll( final List<CupWinner> winners );
}
