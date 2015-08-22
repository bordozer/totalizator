package totalizator.app.services;

import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.dto.*;
import totalizator.app.dto.points.UserCupPointsHolderDTO;
import totalizator.app.dto.points.UserMatchPointsHolderDTO;
import totalizator.app.models.*;

import java.util.List;

public interface DTOService {

	UserDTO transformUser( final User user );

	List<UserDTO> transformUsers( final List<User> users );

	CategoryDTO transformCategory( final Category category );

	List<CategoryDTO> transformCategories( final List<Category> users );

	CupDTO transformCup( final Cup cup, final User user );

	List<CupDTO> transformCups( final List<Cup> cups, final User user );

	TeamDTO transformTeam( final Team team );

	List<TeamDTO> transformTeams( final List<Team> teams );

	MatchDTO transformMatch( final Match match, final User user );

	List<MatchDTO> transformMatches( final List<Match> matches, final User user );

	BetDTO transformMatchBet( final MatchBet matchBet, final User user, final User accessor );

	List<BetDTO> transformMatchBets( final List<MatchBet> matchBets, final User user, final User accessor );

	MatchBetDTO getMatchBetForMatch( final Match match, final User betOfUser, final User accessor );

	List<MatchBetDTO> getMatchBetForMatches( final List<Match> matches, final User betOfUser, final User accessor );

	CupTeamBetDTO transformCupTeamBet( final CupTeamBet cupTeamBet, final User user );

	List<CupTeamBetDTO> transformCupTeamBets( final List<CupTeamBet> cupTeamBets, final User user );

	List<UserGroupDTO> transformUserGroups( final List<UserGroup> userGroups, final User user );

	CupWinnerDTO transformCupWinner( final CupWinner cupWinner, final User accessor );

	List<CupWinnerDTO> transformCupWinners( final List<CupWinner> cupWinners, final User accessor );

	List<PointsCalculationStrategyDTO> transformPCStrategies( final List<PointsCalculationStrategy> strategies );

	UserMatchPointsHolderDTO transformMatchPoints( final UserMatchPointsHolder userMatchPointsHolder );

	UserCupPointsHolderDTO transformCupPoints( final UserCupPointsHolder userCupPointsHolder );
}
