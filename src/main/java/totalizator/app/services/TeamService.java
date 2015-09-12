package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

public interface TeamService extends GenericService<Team> {

	List<Team> loadAll( final Category category );

	Team findByName( final Category category, final String name );

	Team findByImportId( final Category category, final String teamImportId );
}
