package totalizator.app.dao;

import totalizator.app.models.Category;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.util.List;

public interface TeamDao extends GenericService<Team> {

	String CACHE_ENTRY = "totalizator.app.cache.team";
	String CACHE_QUERY = "totalizator.app.cache.teams";

	List<Team> loadAll( final Category category );

	Team findByName( Category category, String name );
}
