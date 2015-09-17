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

	CategoryDTO transformCategory( final Category category, final User user );

	List<CategoryDTO> transformCategories( final List<Category> users, final User user );

	CupDTO transformCup( final Cup cup, final User accessor );

	List<CupDTO> transformCups( final List<Cup> cups, final User accessor );

	TeamDTO transformTeam( final Team team, final User accessor );

	List<TeamDTO> transformTeams( final List<Team> teams, final User accessor );

	MatchDTO transformMatch( final Match match, final User accessor );

	List<MatchDTO> transformMatches( final List<Match> matches, final User accessor );

	BetDTO transformMatchBet( final MatchBet matchBet, final User user, final User accessor );

	List<BetDTO> transformMatchBets( final List<MatchBet> matchBets, final User user, final User accessor );

	MatchBetDTO getMatchBetForMatch( final Match match, final User betOfUser, final User accessor );

	MatchBetDTO getMatchBetForMatch( final Match match, final User betOfUser, final User accessor, final UserGroup userGroup );

	List<MatchBetDTO> getMatchBetForMatches( final List<Match> matches, final User betOfUser, final User accessor );

	CupTeamBetDTO transformCupTeamBet( final CupTeamBet cupTeamBet, final User user );

	List<CupTeamBetDTO> transformCupTeamBets( final List<CupTeamBet> cupTeamBets, final User user );

	List<UserGroupDTO> transformUserGroups( final List<UserGroup> userGroups, final User user );

	CupWinnerDTO transformCupWinner( final CupWinner cupWinner, final User accessor );

	List<CupWinnerDTO> transformCupWinners( final List<CupWinner> cupWinners, final User accessor );

	List<PointsCalculationStrategyDTO> transformPCStrategies( final List<PointsCalculationStrategy> strategies );

	UserMatchPointsHolderDTO transformMatchPoints( final UserMatchPointsHolder userMatchPointsHolder );

	UserCupPointsHolderDTO transformCupPoints( final UserCupPointsHolder userCupPointsHolder );

	SportKindDTO transformSportKind( SportKind sportKind );

	List<SportKindDTO> transformSportKinds( List<SportKind> sportKinds );
}
