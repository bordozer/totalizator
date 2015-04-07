package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.CategoryDTO;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

@Service
public class DTOServiceImpl implements DTOService {

	@Autowired
	private TeamLogoService teamLogoService;

	@Override
	public UserDTO transformUser( final User user ) {
		return userFunction().apply( user );
	}

	@Override
	public List<UserDTO> transformUsers( final List<User> users ) {
		return Lists.transform( users, userFunction() );
	}

	@Override
	public CategoryDTO transformCategory( final Category category ) {
		return categoryFunction().apply( category );
	}

	@Override
	public List<CategoryDTO> transformCategories( final List<Category> users ) {
		return Lists.transform( users, categoryFunction() );
	}

	@Override
	public CupDTO transformCup( final Cup cup ) {
		return cupFunction().apply( cup );
	}

	@Override
	public List<CupDTO> transformCups( final List<Cup> cups ) {
		return Lists.transform( cups, cupFunction() );
	}

	@Override
	public TeamDTO transformTeam( final Team team ) {
		return teamFunction().apply( team );
	}

	@Override
	public List<TeamDTO> transformTeams( final List<Team> teams ) {
		return Lists.transform( teams, teamFunction() );
	}

	private Function<User, UserDTO> userFunction() {

		return new Function<User, UserDTO>() {

			@Override
			public UserDTO apply( final User user ) {
				return new UserDTO( user );
			}
		};
	}

	private Function<Category, CategoryDTO> categoryFunction() {

		return new Function<Category, CategoryDTO>() {

			@Override
			public CategoryDTO apply( final Category category ) {
				return null;
			}
		};
	}

	private Function<Cup, CupDTO> cupFunction() {

		return new Function<Cup, CupDTO>() {

			@Override
			public CupDTO apply( final Cup cup ) {
				final CupDTO cupDTO = new CupDTO( cup.getId(), cup.getCupName(), cup.getCategory().getId() );
				cupDTO.setShowOnPortalPage( cup.isShowOnPortalPage() );

				return cupDTO;
			}
		};
	}

	private Function<Team, TeamDTO> teamFunction() {

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
