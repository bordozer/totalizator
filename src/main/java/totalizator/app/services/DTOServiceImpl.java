package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dto.*;
import totalizator.app.dto.points.UserCupPointsHolderDTO;
import totalizator.app.dto.points.UserMatchPointsHolderDTO;
import totalizator.app.models.*;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.score.UserCupWinnersBonusCalculationService;
import totalizator.app.services.score.UserMatchPointsCalculationService;
import totalizator.app.services.score.UserMatchBetPointsCalculationService;

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
	private UserMatchPointsCalculationService userMatchPointsCalculationService;

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
	public CupDTO transformCup( final Cup cup, final User user ) {
		return cupFunction( user ).apply( cup );
	}

	@Override
	public List<CupDTO> transformCups( final List<Cup> cups, final User user ) {
		return Lists.transform( cups, cupFunction( user ) );
	}

	@Override
	public TeamDTO transformTeam( final Team team ) {
		return teamFunction().apply( team );
	}

	@Override
	public List<TeamDTO> transformTeams( final List<Team> teams ) {
		return Lists.transform( teams, teamFunction() );
	}

	@Override
	public MatchDTO transformMatch( final Match match, final User user ) {
		return matchFunction( user ).apply( match );
	}

	@Override
	public List<MatchDTO> transformMatches( final List<Match> matches, final User user ) {
		return Lists.transform( matches, matchFunction( user ) );
	}

	@Override
	public BetDTO transformMatchBet( final MatchBet matchBet, final User user, final User accessor ) {
		return betFunction( user, accessor ).apply( matchBet );
	}

	@Override
	public MatchBetDTO getMatchBetForMatch( final Match match, final User betOfUser, final User accessor ) {
		return matchBetFunction( betOfUser, accessor ).apply( match );
	}

	@Override
	public List<BetDTO> transformMatchBets( final List<MatchBet> matchBets, final User user, final User accessor ) {
		return newArrayList( Lists.transform( matchBets, betFunction( user, accessor ) ) );
	}

	@Override
	public List<MatchBetDTO> getMatchBetForMatches( final List<Match> matches, final User betOfUser, final User accessor ) {
		return newArrayList( Lists.transform( matches, matchBetFunction( betOfUser, accessor ) ) );
	}

	@Override
	public CupTeamBetDTO transformCupTeamBet( final CupTeamBet cupTeamBet, final User user ) {
		return cupTeamBetFunction( user ).apply( cupTeamBet );
	}

	@Override
	public List<CupTeamBetDTO> transformCupTeamBets( final List<CupTeamBet> cupTeamBets, final User user ) {

		final java.util.function.Function<CupTeamBet, CupTeamBetDTO> mapper = new java.util.function.Function<CupTeamBet, CupTeamBetDTO>() {
			@Override
			public CupTeamBetDTO apply( final CupTeamBet cupTeamBet ) {

				final Cup cup = cupTeamBet.getCup();
				final Team team = cupTeamBet.getTeam();

				final CupTeamBetDTO result = new CupTeamBetDTO();

				result.setCup( DTOServiceImpl.this.transformCup( cup, user ) );
				result.setTeam( DTOServiceImpl.this.transformTeam( team ) );
				result.setUser( DTOServiceImpl.this.transformUser( user ) );

				result.setCupPosition( cupTeamBet.getCupPosition() );

				result.setPoints( userCupWinnersBonusCalculationService.getUserCupWinnersPoints( cup, team, user, cupTeamBet.getCupPosition() ) );

				result.setStillActive( cupTeamService.exists( cup.getId(), team.getId() ) );

				return result;
			}
		};
		return cupTeamBets.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	public List<UserGroupDTO> transformUserGroups( final List<UserGroup> userGroups, final User user ) {

		final java.util.function.Function<Cup, Integer> cupMapper = new java.util.function.Function<Cup, Integer>() {
			@Override
			public Integer apply( final Cup cup ) {
				return cup.getId();
			}
		};

		final java.util.function.Function<? super UserGroup, UserGroupDTO> mapper = new java.util.function.Function<UserGroup, UserGroupDTO>() {

			@Override
			public UserGroupDTO apply( final UserGroup userGroup ) {

				final UserGroupDTO userGroupDTO = new UserGroupDTO();
				userGroupDTO.setUserGroupId( userGroup.getId() );
				userGroupDTO.setUserGroupName( userGroup.getGroupName() );
				userGroupDTO.setUserGroupOwner( transformUser( userGroup.getOwner() ) );

				userGroupDTO.setUserGroupCups( transformCups( userGroupService.loadCups( userGroup ), user ) );

				return userGroupDTO;
			}
		};

		return userGroups.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	public CupWinnerDTO transformCupWinner( final CupWinner cupWinner, final User accessor ) {
		return cupWinnerFunction( accessor ).apply( cupWinner );
	}

	@Override
	public List<CupWinnerDTO> transformCupWinners( final List<CupWinner> cupWinners, final User accessor ) {

		final java.util.function.Function<? super CupWinner, CupWinnerDTO> mapper = new java.util.function.Function<CupWinner, CupWinnerDTO>() {

			@Override
			public CupWinnerDTO apply( final CupWinner cupWinner ) {
				return cupWinnerFunction( accessor ).apply( cupWinner );
			}
		};
		return cupWinners.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	public List<PointsCalculationStrategyDTO> transformPCStrategies( final List<PointsCalculationStrategy> strategies ) {

		final java.util.function.Function<PointsCalculationStrategy, PointsCalculationStrategyDTO> mapper = new java.util.function.Function<PointsCalculationStrategy, PointsCalculationStrategyDTO>() {

			@Override
			public PointsCalculationStrategyDTO apply( final PointsCalculationStrategy strategy ) {

				final PointsCalculationStrategyDTO dto = new PointsCalculationStrategyDTO();
				dto.setPcsId( strategy.getId() );
				dto.setStrategyName( strategy.getStrategyName() );
				dto.setPointsForMatchScore( strategy.getPointsForMatchScore() );
				dto.setPointsForMatchWinner( strategy.getPointsForMatchWinner() );
				dto.setPointsDelta( strategy.getPointsDelta() );
				dto.setPointsForBetWithinDelta( strategy.getPointsForBetWithinDelta() );

				return dto;
			}
		};

		return strategies.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	public UserMatchPointsHolderDTO transformMatchPoints( final UserMatchPointsHolder userMatchPointsHolder ) {
		return userMatchPointsFunction().apply( userMatchPointsHolder );
	}

	@Override
	public UserCupPointsHolderDTO transformCupPoints( final UserCupPointsHolder userCupPointsHolder ) {
		return userCupPointsFunction().apply( userCupPointsHolder );
	}

	private Function<UserMatchPointsHolder, UserMatchPointsHolderDTO> userMatchPointsFunction() {

		return new Function<UserMatchPointsHolder, UserMatchPointsHolderDTO>() {

			@Override
			public UserMatchPointsHolderDTO apply( final UserMatchPointsHolder userMatchPoints ) {
				return new UserMatchPointsHolderDTO( userMatchPoints.getUserMatchBetPointsHolder().getPoints(), userMatchPoints.getMatchBonus() );
			}
		};
	}

	private Function<UserCupPointsHolder, UserCupPointsHolderDTO> userCupPointsFunction() {

		return new Function<UserCupPointsHolder, UserCupPointsHolderDTO>() {

			@Override
			public UserCupPointsHolderDTO apply( final UserCupPointsHolder userCupPointsHolder ) {
				return new UserCupPointsHolderDTO( transformUser( userCupPointsHolder.getUser() ), userCupPointsHolder.getMatchBetPoints(), userCupPointsHolder.getMatchBonuses(), userCupPointsHolder.getCupWinnerBonus() );
			}
		};
	}

	private Function<CupWinner, CupWinnerDTO> cupWinnerFunction( final User accessor ) {

		return new Function<CupWinner, CupWinnerDTO>() {

			@Override
			public CupWinnerDTO apply( final CupWinner cupWinner ) {
				return new CupWinnerDTO( transformCup( cupWinner.getCup(), accessor ), cupWinner.getCupPosition(), transformTeam( cupWinner.getTeam() ) );
			}
		};
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
				final CategoryDTO categoryDTO = new CategoryDTO( category.getId(), category.getCategoryName() );
				categoryDTO.setLogoUrl( logoService.getLogoURL( category ) );
				return categoryDTO;
			}
		};
	}

	private Function<Cup, CupDTO> cupFunction( final User user ) {

		return new Function<Cup, CupDTO>() {

			@Override
			public CupDTO apply( final Cup cup ) {

				final String cupName = cup.isPublicCup() ? cup.getCupName() : String.format( "[ %s ]", cup.getCupName() );
				final CupDTO cupDTO = new CupDTO( cup.getId(), cupName, transformCategory( cup.getCategory() ) );

				cupDTO.setWinnersCount( cup.getWinnersCount() );
				cupDTO.setCupStartDate( cup.getCupStartTime() );
				cupDTO.setLogoUrl( logoService.getLogoURL( cup ) );

				cupDTO.setReadyForCupBets( !cupBetsService.isCupBettingFinished( cup ) );
				cupDTO.setReadyForMatchBets( !cupBetsService.isMatchBettingFinished( cup ) );

				cupDTO.setCupStarted( cupService.isCupStarted( cup ) );
				cupDTO.setFinished( cupService.isCupFinished( cup ) );

				cupDTO.setCupBetAllowance( cupBetsService.validateBettingAllowed( cup ) );

				return cupDTO;
			}
		};
	}

	private Function<Team, TeamDTO> teamFunction() {

		return new Function<Team, TeamDTO>() {

			@Override
			public TeamDTO apply( final Team team ) {
				return new TeamDTO( team.getId(), team.getTeamName(), transformCategory( team.getCategory() ), logoService.getLogoURL( team ) );
			}
		};
	}

	private Function<Match, MatchDTO> matchFunction( final User user ) {

		return new Function<Match, MatchDTO>() {

			@Override
			public MatchDTO apply( final Match match ) {
				final MatchDTO dto = new MatchDTO();

				dto.setMatchId( match.getId() );
				dto.setCategory( transformCategory( match.getCup().getCategory() ) );
				dto.setCup( transformCup( match.getCup(), user ) );

				dto.setTeam1( transformTeam( match.getTeam1() ) );
				dto.setScore1( match.getScore1() );

				dto.setTeam2( transformTeam( match.getTeam2() ) );
				dto.setScore2( match.getScore2() );

				dto.setBeginningTime( match.getBeginningTime() );

				dto.setMatchFinished( match.isMatchFinished() );
				dto.setMatchStarted( matchService.isMatchStarted( match ) );

				dto.setShowAnotherBets( matchBetsService.userCanSeeAnotherBets( match, user )  );

				dto.setHomeTeamNumber( match.getHomeTeamNumber() );
				dto.setDescription( match.getDescription() );

				return dto;
			}
		};
	}

	private Function<MatchBet, BetDTO> betFunction( final User user, final User accessor ) {

		return new Function<MatchBet, BetDTO>() {

			@Override
			public BetDTO apply( final MatchBet matchBet ) {

				final MatchDTO matchDTO = transformMatch( matchBet.getMatch(), user );

				final BetDTO betDTO = new BetDTO( matchDTO, transformUser( user ) );
				betDTO.setMatchBetId( matchBet.getId() );

				final boolean isSecuredBet = ! matchBetsService.isAllowedToShowMatchBets( matchBet, accessor );
				if ( ! isSecuredBet ) {
					betDTO.setScore1( matchBet.getBetScore1() );
					betDTO.setScore2( matchBet.getBetScore2() );
				}
				betDTO.setSecuredBet( isSecuredBet );

				return betDTO;
			}
		};
	}

	private Function<Match, MatchBetDTO> matchBetFunction( final User betsOfUser, final User accessor ) {

		return new Function<Match, MatchBetDTO>() {

			@Override
			public MatchBetDTO apply( final Match match ) {
				final MatchDTO matchDTO = transformMatch( match, betsOfUser );

				final MatchBetDTO matchBetDTO = new MatchBetDTO( matchDTO );

				final ValidationResult validationResult = matchBetsService.validateBettingAllowed( match, betsOfUser );
				matchBetDTO.setBettingAllowed( validationResult.isPassed() );
				matchBetDTO.setBettingValidationMessage( validationResult.getMessage() );

				final MatchBet matchBet = matchBetsService.load( betsOfUser, match );

				if ( matchBet == null ) {
					return matchBetDTO;
				}

				final BetDTO betDTO = transformMatchBet( matchBet, betsOfUser, accessor );

				matchBetDTO.setBet( betDTO );
				matchBetDTO.setUserMatchPointsHolder( userMatchPointsFunction().apply( userMatchPointsCalculationService.getUserMatchPoints( matchBet ) ) );

				return matchBetDTO;
			}
		};
	}

	private Function<CupTeamBet, CupTeamBetDTO> cupTeamBetFunction( final User user ) {

		return new Function<CupTeamBet, CupTeamBetDTO>() {
			@Override
			public CupTeamBetDTO apply( final CupTeamBet cupTeamBet ) {

				final Cup cup = cupTeamBet.getCup();
				final Team team = cupTeamBet.getTeam();

				final CupTeamBetDTO result = new CupTeamBetDTO();

				result.setCup( transformCup( cup, user ) );
				result.setTeam( transformTeam( team ) );
				result.setUser( transformUser( user ) );

				result.setCupPosition( cupTeamBet.getCupPosition() );

				result.setPoints( userCupWinnersBonusCalculationService.getUserCupWinnersPoints( cup, team, user, cupTeamBet.getCupPosition() ) );

				result.setStillActive( cupTeamService.exists( cup.getId(), team.getId() ) );

				return result;
			}
		};
	}
}
