package totalizator.app.services;

import totalizator.app.dto.CategoryDTO;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

public interface DTOService {


	UserDTO transformUser( final User user );

	List<UserDTO> transformUsers( final List<User> users );

	CategoryDTO transformCategory( final Category category );

	List<CategoryDTO> transformCategories( final List<Category> users );

	CupDTO transformCup( final Cup cup );

	List<CupDTO> transformCups( final List<Cup> cups );

	TeamDTO transformTeam( final Team team );

	List<TeamDTO> transformTeams( final List<Team> teams );
}
