package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

@Service
public class DTOServiceImpl implements DTOService {

	@Autowired
	private TeamLogoService teamLogoService;

	@Override
	public TeamDTO transform( final Team team ) {

		return userFunction().apply( team );
	}

	@Override
	public List<TeamDTO> transform( final List<Team> teams ) {
		return Lists.transform( teams, userFunction() );
	}

	private Function<Team, TeamDTO> userFunction() {
		return new Function<Team, TeamDTO>() {

			@Override
			public TeamDTO apply( final Team team ) {
				final Category category = team.getCategory();
				final String teamLogoURL = teamLogoService.getTeamLogoURL( team );

				return new TeamDTO( team.getId(), team.getTeamName(), category.getId(), teamLogoURL );
			}
		};
	}
}
