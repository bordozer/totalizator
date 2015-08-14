package totalizator.app.services;

import org.springframework.transaction.annotation.Transactional;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

public interface TeamService extends GenericService<Team> {

	List<Team> loadAll( final Category category );

	@Transactional( readOnly = true )
	Team findByName( Category category, String name );
}
