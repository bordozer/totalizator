package totalizator.app.services;

import totalizator.app.dto.*;
import totalizator.app.enums.CupPosition;
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

	MatchDTO transformMatch( final Match match );

	List<MatchDTO> transformMatches( final List<Match> matches );

	BetDTO transformMatchBet( final MatchBet matchBet, final User user );

	MatchBetDTO getMatchBetForMatch( final Match match, final User user );

	List<MatchBetDTO> getMatchBetForMatches( final List<Match> matches, final User user );

	void initMatchFromDTO( final MatchDTO matchDTO, final Match match );

	CupTeamBetDTO transformCupTeamBet( final CupTeamBet cupTeamBet );

	List<CupTeamBetDTO> transformCupTeamBets( final List<CupTeamBet> cupTeamBets );

	CupPositionDTO transformCupPosition( final CupPosition cupPosition );
}
