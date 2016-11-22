package betmen.web.converters;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.SportKind;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.model.MatchSearchModel;
import betmen.core.model.points.UserCupPointsHolder;
import betmen.core.model.points.UserMatchPointsHolder;
import betmen.dto.dto.BetDTO;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.CupItemDTO;
import betmen.dto.dto.CupTeamBetDTO;
import betmen.dto.dto.CupWinnerDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.PointsCalculationStrategyDTO;
import betmen.dto.dto.SportKindDTO;
import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserGroupDTO;
import betmen.dto.dto.points.UserCupPointsHolderDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;

import java.util.List;

public interface DTOService {

    UserDTO transformUser(final User user);

    List<UserDTO> transformUsers(final List<User> users);

    CategoryDTO transformCategory(final Category category);

    List<CategoryDTO> transformCategories(final List<Category> categories);

    FavoriteCategoryDTO transformFavoriteCategory(final Category category, final User user);

    List<FavoriteCategoryDTO> transformFavoriteCategories(final List<Category> categories, final User user);

    CupDTO transformCup(final Cup cup, final User accessor);

    List<CupDTO> transformCups(final List<Cup> cups, final User accessor);

    CupItemDTO transformCupItem(final Cup cup);

    List<CupItemDTO> transformCupItems(final List<Cup> cups);

    TeamDTO transformTeam(final Team team, final User accessor);

    List<TeamDTO> transformTeams(final Category category, final List<Team> teams, final User accessor);

    MatchDTO transformMatch(final Match match, final User accessor);

    List<MatchDTO> transformMatches(final List<Match> matches, final User accessor);

    BetDTO transformMatchBet(final MatchBet matchBet, final User user, final User accessor);

    List<BetDTO> transformMatchBets(final List<MatchBet> matchBets, final User user, final User accessor);

    MatchBetDTO getMatchBetForMatch(final Match match, final User betOfUser, final User accessor);

    MatchBetDTO getMatchBetForMatch(final Match match, final User betOfUser, final User accessor, final UserGroupEntity userGroupEntity);

    List<MatchBetDTO> getMatchBetForMatches(final List<Match> matches, final User betOfUser, final User accessor);

    CupTeamBetDTO transformCupTeamBet(final CupTeamBet cupTeamBet, final User user);

    List<CupTeamBetDTO> transformCupTeamBets(final List<CupTeamBet> cupTeamBets, final User user);

    List<UserGroupDTO> transformUserGroups(final List<UserGroupEntity> userGroupEntities, final User user);

    CupWinnerDTO transformCupWinner(final CupWinner cupWinner, final User accessor);

    List<CupWinnerDTO> transformCupWinners(final List<CupWinner> cupWinners, final User accessor);

    List<PointsCalculationStrategyDTO> transformPCStrategies(final List<PointsCalculationStrategy> strategies);

    UserMatchPointsHolderDTO transformMatchPoints(final UserMatchPointsHolder userMatchPointsHolder);

    UserCupPointsHolderDTO transformCupPoints(final UserCupPointsHolder userCupPointsHolder);

    SportKindDTO transformSportKind(SportKind sportKind);

    List<SportKindDTO> transformSportKinds(List<SportKind> sportKinds);

    MatchSearchModel transformMatchSearchModel(MatchSearchModelDto dto);
}
