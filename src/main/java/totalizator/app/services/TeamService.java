package totalizator.app.services;

import com.google.common.base.Function;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

public interface TeamService extends GenericService<Team>, NamedEntityGenericService<Team> {

	Function<Team, TeamDTO> TEAM_TO_TEAM_DTO_FUNCTION = new Function<Team, TeamDTO>() {
		@Override
		public TeamDTO apply( final Team team ) {
			final Category category = team.getCategory();
			return new TeamDTO( team.getId(), team.getTeamName(), category.getId() );
		}
	};

	List<Team> loadAll( Category category );
}
