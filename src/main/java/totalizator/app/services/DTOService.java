package totalizator.app.services;

import totalizator.app.dto.*;
import totalizator.app.models.*;

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

	MatchDTO transformMatch( Match match );

	List<MatchDTO> transformMatch( List<Match> matches );
}
