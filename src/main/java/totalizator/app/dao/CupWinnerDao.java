package totalizator.app.dao;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.util.List;

public interface CupWinnerDao extends GenericService<CupWinner> {

	String CACHE_ENTRY = "totalizator.app.cache.cup-winner";
	String CACHE_QUERY = "totalizator.app.cache.cup-winner.query";

	List<CupWinner> loadAll( Cup cup );

	@Cacheable( value = CACHE_QUERY )
	List<CupWinner> loadAll( Cup cup, Team team );

	void saveAll( List<CupWinner> winners );
}
