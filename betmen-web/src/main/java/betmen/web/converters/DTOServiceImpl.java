package betmen.web.converters;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.MatchPoints;
import betmen.core.entity.PointsCalculationStrategy;
import betmen.core.entity.SportKind;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.exception.BadRequestException;
import betmen.core.model.MatchSearchModel;
import betmen.core.model.ValidationResult;
import betmen.core.model.points.UserCupPointsHolder;
import betmen.core.model.points.UserMatchPointsHolder;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.CupTeamService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.LogoService;
import betmen.core.service.UserGroupService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.points.MatchPointsService;
import betmen.core.service.points.calculation.cup.UserCupWinnersBonusCalculationService;
import betmen.core.service.points.calculation.match.points.UserMatchBetPointsCalculationService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.BetDTO;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.CupDTO;
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
import betmen.dto.dto.ValidationResultDto;
import betmen.dto.dto.points.UserCupPointsHolderDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class DTOServiceImpl implements DTOService {

    @Autowired
    private LogoService logoService;

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchPointsService matchPointsService;

    @Autowired
    private UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

    @Autowired
    private UserCupWinnersBonusCalculationService userCupWinnersBonusCalculationService;

    @Autowired
    private CupBetsService cupBetsService;

    @Autowired
    private CupService cupService;

    @Autowired
    private CupTeamService cupTeamService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private FavoriteCategoryService favoriteCategoryService;

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public UserDTO transformUser(final User user) {
        return userFunction().apply(user);
    }

    @Override
    public List<UserDTO> transformUsers(final List<User> users) {
        return Lists.transform(users, userFunction());
    }

    @Override
    public CategoryDTO transformCategory(final Category category) {
        return convertToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> transformCategories(final List<Category> categories) {
        return categories.stream().map(category -> convertToCategoryDTO(category)).collect(Collectors.toList());
    }

    @Override
    public FavoriteCategoryDTO transformFavoriteCategory(final Category category, final User user) {
        return convertToFavoriteCategoryDTO(user, category);
    }

    @Override
    public List<FavoriteCategoryDTO> transformFavoriteCategories(final List<Category> categories, final User user) {
        return categories.stream().map(category -> convertToFavoriteCategoryDTO(user, category)).collect(Collectors.toList());
    }

    @Override
    public CupDTO transformCup(final Cup cup, final User accessor) {
        return cupFunction(accessor).apply(cup);
    }

    @Override
    public List<CupDTO> transformCups(final List<Cup> cups, final User accessor) {
        return Lists.transform(cups, cupFunction(accessor));
    }

    @Override
    public TeamDTO transformTeam(final Team team, final User accessor) {
        return teamFunction(team.getCategory(), accessor).apply(team);
    }

    @Override
    public List<TeamDTO> transformTeams(final Category category, final List<Team> teams, final User accessor) {
        return Lists.transform(teams, teamFunction(category, accessor));
    }

    @Override
    public MatchDTO transformMatch(final Match match, final User accessor) {
        return matchFunction(accessor).apply(match);
    }

    @Override
    public List<MatchDTO> transformMatches(final List<Match> matches, final User accessor) {
        return Lists.transform(matches, matchFunction(accessor));
    }

    @Override
    public BetDTO transformMatchBet(final MatchBet matchBet, final User user, final User accessor) {
        return betFunction(user, accessor).apply(matchBet);
    }

    @Override
    public MatchBetDTO getMatchBetForMatch(final Match match, final User betOfUser, final User accessor) {
        return matchBetDTOFunction(betOfUser, accessor).apply(match);
    }

    @Override
    public MatchBetDTO getMatchBetForMatch(final Match match, final User betOfUser, final User accessor, final UserGroup userGroup) {
        return matchBetDTOFunctionForUserGroup(betOfUser, accessor, userGroup).apply(match);
    }

    @Override
    public List<BetDTO> transformMatchBets(final List<MatchBet> matchBets, final User user, final User accessor) {
        return newArrayList(Lists.transform(matchBets, betFunction(user, accessor)));
    }

    @Override
    public List<MatchBetDTO> getMatchBetForMatches(final List<Match> matches, final User betOfUser, final User accessor) {
        return newArrayList(Lists.transform(matches, matchBetDTOFunction(betOfUser, accessor)));
    }

    @Override
    public CupTeamBetDTO transformCupTeamBet(final CupTeamBet cupTeamBet, final User user) {
        return cupTeamBetFunction(user).apply(cupTeamBet);
    }

    @Override
    public List<CupTeamBetDTO> transformCupTeamBets(final List<CupTeamBet> cupTeamBets, final User user) {
        return cupTeamBets.stream().map(cupTeamBet -> {

            final Cup cup = cupTeamBet.getCup();
            final Team team = cupTeamBet.getTeam();

            final CupTeamBetDTO result = new CupTeamBetDTO();

            result.setCup(this.transformCup(cup, user));
            result.setTeam(this.transformTeam(team, user));
            result.setUser(this.transformUser(user));

            result.setCupPosition(cupTeamBet.getCupPosition());

            result.setPoints(userCupWinnersBonusCalculationService.getUserCupWinnerPoints(cup, team, user, cupTeamBet.getCupPosition()));

            result.setStillActive(cupTeamService.exists(cup.getId(), team.getId()));

            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserGroupDTO> transformUserGroups(final List<UserGroup> userGroups, final User user) {
        return userGroups.stream().map(userGroup -> {
            final UserGroupDTO userGroupDTO = new UserGroupDTO();
            userGroupDTO.setUserGroupId(userGroup.getId());
            userGroupDTO.setUserGroupName(userGroup.getGroupName());
            userGroupDTO.setUserGroupOwner(transformUser(userGroup.getOwner()));

            userGroupDTO.setUserGroupCups(transformCups(userGroupService.loadCups(userGroup), user));

            return userGroupDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public CupWinnerDTO transformCupWinner(final CupWinner cupWinner, final User accessor) {
        return cupWinnerFunction(accessor).apply(cupWinner);
    }

    @Override
    public List<CupWinnerDTO> transformCupWinners(final List<CupWinner> cupWinners, final User accessor) {
        return cupWinners.stream().map(cupWinner -> cupWinnerFunction(accessor).apply(cupWinner)).collect(Collectors.toList());
    }

    @Override
    public List<PointsCalculationStrategyDTO> transformPCStrategies(final List<PointsCalculationStrategy> strategies) {

        return strategies.stream()
                .map(strategy -> {
                    final PointsCalculationStrategyDTO dto = new PointsCalculationStrategyDTO();
                    dto.setPcsId(strategy.getId());
                    dto.setStrategyName(strategy.getStrategyName());
                    dto.setPointsForMatchScore(strategy.getPointsForMatchScore());
                    dto.setPointsForMatchWinner(strategy.getPointsForMatchWinner());
                    dto.setPointsDelta(strategy.getPointsDelta());
                    dto.setPointsForBetWithinDelta(strategy.getPointsForBetWithinDelta());

                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public UserMatchPointsHolderDTO transformMatchPoints(final UserMatchPointsHolder userMatchPointsHolder) {
        return userMatchPointsFunction().apply(userMatchPointsHolder);
    }

    @Override
    public UserCupPointsHolderDTO transformCupPoints(final UserCupPointsHolder userCupPointsHolder) {
        return userCupPointsFunction().apply(userCupPointsHolder);
    }

    @Override
    public SportKindDTO transformSportKind(final SportKind sportKind) {
        return sportKindsFunction().apply(sportKind);
    }

    @Override
    public List<SportKindDTO> transformSportKinds(final List<SportKind> sportKinds) {
        return sportKinds
                .stream()
                .map(sportKindsFunction())
                .collect(Collectors.toList());
    }

    @Override
    public MatchSearchModel transformMatchSearchModel(MatchSearchModelDto dto) {
        if (dto.isFilterByDateEnable() && !dateTimeService.isValidDate(dto.getFilterByDate())) {
            throw new BadRequestException(String.format("Date '%s' is invalid", dto.getFilterByDate()));
        }
        final MatchSearchModel model = new MatchSearchModel();
        model.setCategoryId(dto.getCategoryId());
        model.setCupId(dto.getCupId());
        model.setTeamId(dto.getTeamId());
        model.setTeam2Id(dto.getTeam2Id());
        model.setFilterByDateEnable(dto.isFilterByDateEnable());
        model.setFilterByDate(dateTimeService.parseDate(dto.getFilterByDate()));
        model.setShowFutureMatches(dto.isShowFutureMatches());
        model.setShowFinished(dto.isShowFinished());
        model.setSorting(dto.getSorting());
        model.setUserId(dto.getUserId());

        return model;
    }

    private Function<UserMatchPointsHolder, UserMatchPointsHolderDTO> userMatchPointsFunction() {
        return userMatchPoints -> new UserMatchPointsHolderDTO(transformUser(userMatchPoints.getUser()), userMatchPoints.getMatchBetPoints(), userMatchPoints.getMatchBonus());
    }

    private Function<UserCupPointsHolder, UserCupPointsHolderDTO> userCupPointsFunction() {
        return userCupPointsHolder -> {
            final Cup cup = userCupPointsHolder.getCup();
            final User user = userCupPointsHolder.getUser();

            final UserCupPointsHolderDTO dto = new UserCupPointsHolderDTO(transformUser(user), userCupPointsHolder.getMatchBetPoints(), userCupPointsHolder.getMatchBonuses(), userCupPointsHolder.getCupWinnerBonus());
            dto.setMatchBetPointsNegative(userMatchBetPointsCalculationService.getUserMatchBetPointsNegative(cup, user));
            dto.setMatchBetPointsPositive(userMatchBetPointsCalculationService.getUserMatchBetPointsPositive(cup, user));

            return dto;
        };
    }

    private Function<CupWinner, CupWinnerDTO> cupWinnerFunction(final User accessor) {
        return cupWinner -> new CupWinnerDTO(transformCup(cupWinner.getCup(), accessor), cupWinner.getCupPosition(), transformTeam(cupWinner.getTeam(), accessor));
    }

    private Function<User, UserDTO> userFunction() {
        return user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getId());
            userDTO.setUserName(StringEscapeUtils.escapeJavaScript(user.getUsername()));
            return userDTO;
        };
    }

    private CategoryDTO convertToCategoryDTO(final Category category) {
        final CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getCategoryName());
        categoryDTO.setLogoUrl(logoService.getLogoURL(category));
        if (category.getSportKind() != null) {
            categoryDTO.setSportKind(transformSportKind(category.getSportKind()));
        }
        return categoryDTO;
    }

    private FavoriteCategoryDTO convertToFavoriteCategoryDTO(final User user, final Category category) {
        final FavoriteCategoryDTO categoryDTO = new FavoriteCategoryDTO(category.getId(), category.getCategoryName());
        categoryDTO.setLogoUrl(logoService.getLogoURL(category));
        categoryDTO.setFavoriteCategory(favoriteCategoryService.isInFavorites(user, category));
        if (category.getSportKind() != null) {
            categoryDTO.setSportKind(transformSportKind(category.getSportKind()));
        }
        return categoryDTO;
    }

    private Function<Cup, CupDTO> cupFunction(final User user) {
        return cup -> {
            final String cupName = cup.isPublicCup() ? cup.getCupName() : String.format("[ %s ]", cup.getCupName());
            final CupDTO cupDTO = new CupDTO(cup.getId(), cupName, transformCategory(cup.getCategory()));

            cupDTO.setWinnersCount(cup.getWinnersCount());
            cupDTO.setCupStartDate(cup.getCupStartTime());
            cupDTO.setLogoUrl(logoService.getLogoURL(cup));

            cupDTO.setReadyForCupBets(!cupBetsService.isCupBettingFinished(cup));
            cupDTO.setReadyForMatchBets(!cupBetsService.isMatchBettingFinished(cup));

            cupDTO.setCupStarted(cupService.isCupStarted(cup));
            cupDTO.setFinished(cupService.isCupFinished(cup));

            cupDTO.setCupBetAllowance(convertValidationResult(cup));

            return cupDTO;
        };
    }

    private ValidationResultDto convertValidationResult(final Cup cup) {
        ValidationResult validationResult = cupBetsService.validateBettingAllowed(cup);
        return new ValidationResultDto(validationResult.isPassed(), validationResult.getErrorCode());
    }

    private Function<Team, TeamDTO> teamFunction(final Category category, final User accessor) {
        final CategoryDTO categoryDTO = transformCategory(category);
        return team -> new TeamDTO(team.getId(), team.getTeamName(), categoryDTO, logoService.getLogoURL(team));
    }

    private Function<Match, MatchDTO> matchFunction(final User accessor) {
        return match -> {
            final MatchDTO dto = new MatchDTO();

            dto.setMatchId(match.getId());
            dto.setCategory(transformCategory(match.getCup().getCategory()));
            dto.setCup(transformCup(match.getCup(), accessor));

            dto.setTeam1(transformTeam(match.getTeam1(), accessor));
            dto.setScore1(match.getScore1());

            dto.setTeam2(transformTeam(match.getTeam2(), accessor));
            dto.setScore2(match.getScore2());

            dto.setBeginningTime(match.getBeginningTime());

            dto.setMatchFinished(match.isMatchFinished());
            dto.setMatchStarted(matchService.isMatchStarted(match));

            dto.setShowAnotherBets(matchBetsService.userCanSeeAnotherBets(match, accessor));

            dto.setHomeTeamNumber(match.getHomeTeamNumber());
            dto.setDescription(match.getDescription());

            return dto;
        };
    }

    private Function<MatchBet, BetDTO> betFunction(final User user, final User accessor) {
        return matchBet -> {
            final MatchDTO matchDTO = transformMatch(matchBet.getMatch(), user);
            final BetDTO betDTO = new BetDTO(matchDTO, transformUser(user));
            betDTO.setMatchBetId(matchBet.getId());
            final boolean isSecuredBet = !matchBetsService.isAllowedToShowMatchBets(matchBet, accessor);
            if (!isSecuredBet) {
                betDTO.setScore1(matchBet.getBetScore1());
                betDTO.setScore2(matchBet.getBetScore2());
            }
            betDTO.setSecuredBet(isSecuredBet);
            return betDTO;
        };
    }

    private Function<Match, MatchBetDTO> matchBetDTOFunction(final User betsOfUser, final User accessor) {
        return match -> getMatchBetDTO(match, betsOfUser, accessor, matchPointsService.load(betsOfUser, match));
    }

    private Function<Match, MatchBetDTO> matchBetDTOFunctionForUserGroup(final User betsOfUser, final User accessor, final UserGroup userGroup) {
        return match -> getMatchBetDTO(match, betsOfUser, accessor, matchPointsService.load(betsOfUser, match, userGroup));
    }

    private MatchBetDTO getMatchBetDTO(final Match match, final User betsOfUser, final User accessor, final MatchPoints pointsHolder) {

        final MatchDTO matchDTO = transformMatch(match, betsOfUser);

        final MatchBetDTO matchBetDTO = new MatchBetDTO(matchDTO);

        final ValidationResult validationResult = matchBetsService.validateBettingAllowed(match, betsOfUser);
        matchBetDTO.setBettingAllowed(validationResult.isPassed());
        matchBetDTO.setBettingValidationMessage(validationResult.getErrorCode());
        matchBetDTO.setTotalBets(matchBetsService.betsCount(match.getId()));

        final MatchBet matchBet = matchBetsService.load(betsOfUser, match);

        if (matchBet == null) {
            return matchBetDTO;
        }

        final BetDTO betDTO = transformMatchBet(matchBet, betsOfUser, accessor);

        matchBetDTO.setBet(betDTO);

        if (pointsHolder != null) {
            matchBetDTO.setUserMatchPointsHolder(new UserMatchPointsHolderDTO(transformUser(pointsHolder.getUser()), pointsHolder.getMatchPoints(), pointsHolder.getMatchBonus()));
        } else {
            matchBetDTO.setUserMatchPointsHolder(new UserMatchPointsHolderDTO(transformUser(betsOfUser), 0, 0));
        }

        return matchBetDTO;
    }

    private Function<CupTeamBet, CupTeamBetDTO> cupTeamBetFunction(final User user) {
        return cupTeamBet -> {
            final Cup cup = cupTeamBet.getCup();
            final Team team = cupTeamBet.getTeam();

            final CupTeamBetDTO result = new CupTeamBetDTO();

            result.setCup(transformCup(cup, user));
            result.setTeam(transformTeam(team, user));
            result.setUser(transformUser(user));

            result.setCupPosition(cupTeamBet.getCupPosition());

            result.setPoints(userCupWinnersBonusCalculationService.getUserCupWinnerPoints(cup, team, user, cupTeamBet.getCupPosition()));

            result.setStillActive(cupTeamService.exists(cup.getId(), team.getId()));

            return result;
        };
    }

    private java.util.function.Function<SportKind, SportKindDTO> sportKindsFunction() {
        return sportKind -> new SportKindDTO(sportKind.getId(), sportKind.getSportKindName());
    }
}
