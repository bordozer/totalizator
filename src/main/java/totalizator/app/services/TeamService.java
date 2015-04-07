package totalizator.app.services;

import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

public interface TeamService extends GenericService<Team>, NamedEntityGenericService<Team> {

	List<Team> loadAll( final Category category );
}
