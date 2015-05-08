package totalizator.app.dao;

import totalizator.app.models.Category;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import java.util.List;

public interface TeamDao extends GenericService<Team>, NamedEntityGenericService<Team> {

	String CACHE_ENTRY = "totalizator.app.cache.team";
	String CACHE_QUERY = "totalizator.app.cache.teams";

	List<Team> loadAll( Category category );
}
