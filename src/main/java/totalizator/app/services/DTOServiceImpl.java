package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dto.*;
import totalizator.app.models.*;
import totalizator.app.services.score.CupScoresService;

import java.util.List;

@Service
public class DTOServiceImpl implements DTOService {

	@Autowired
	private LogoService logoService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private CupScoresService cupScoresService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private CupWinnerService cupWinnerService;

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
	public BetDTO transformMatchBet( final MatchBet matchBet, final User user ) {
		return betFunction( user ).apply( matchBet );
	}

	@Override
	public MatchBetDTO getMatchBetForMatch( final Match match, final User user ) {
		return matchBetFunction( user ).apply( match );
	}

	@Override
	public List<MatchBetDTO> getMatchBetForMatches( final List<Match> matches, final User user ) {
		return Lists.transform( matches, matchBetFunction( user ) );
	}

	@Override
	public CupTeamBetDTO transformCupTeamBet( final CupTeamBet cupTeamBet, final User user ) {
		return cupTeamBetFunction( user ).apply( cupTeamBet );
	}

	@Override
	public List<CupTeamBetDTO> transformCupTeamBets( final List<CupTeamBet> cupTeamBets, final User user ) {
		return Lists.transform( cupTeamBets, cupTeamBetFunction( user ) );
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
				final CupDTO cupDTO = new CupDTO( cup.getId(), cup.getCupName(), transformCategory( cup.getCategory() ) );
				cupDTO.setShowOnPortalPage( cup.isShowOnPortalPage() );
				cupDTO.setWinnersCount( cup.getWinnersCount() );
				cupDTO.setCupStartDate( cup.getCupStartTime() );
				cupDTO.setLogoUrl( logoService.getLogoURL( cup ) );

				cupDTO.setReadyForCupBets( ! cupBetsService.isCupBettingFinished( cup ) );
				cupDTO.setReadyForMatchBets( ! cupBetsService.isMatchBettingFinished( cup ) );
				cupDTO.setFinished( cupBetsService.isCupFinished( cup ) );

				cupDTO.setCupBettingIsAllowed( ! cupBetsService.isCupBettingFinished( cup ) );

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

				return dto;
			}
		};
	}

	private Function<MatchBet, BetDTO> betFunction( final User user ) {

		return new Function<MatchBet, BetDTO>() {

			@Override
			public BetDTO apply( final MatchBet matchBet ) {

				final MatchDTO matchDTO = transformMatch( matchBet.getMatch(), user );

				final BetDTO betDTO = new BetDTO( matchDTO, transformUser( user ) );
				betDTO.setMatchBetId( matchBet.getId() );
				betDTO.setScore1( matchBet.getBetScore1() );
				betDTO.setScore2( matchBet.getBetScore2() );

				return betDTO;
			}
		};
	}

	private Function<Match, MatchBetDTO> matchBetFunction( final User user ) {

		return new Function<Match, MatchBetDTO>() {

			@Override
			public MatchBetDTO apply( final Match match ) {
				final MatchDTO matchDTO = transformMatch( match, user );

				final MatchBetDTO matchBetDTO = new MatchBetDTO( matchDTO );

				final ValidationResult validationResult = matchBetsService.validateBettingAllowed( match, user );
				matchBetDTO.setBettingAllowed( validationResult.isPassed() );
				matchBetDTO.setBettingValidationMessage( validationResult.getMessage() );

				final MatchBet matchBet = matchBetsService.load( user, match );

				matchBetDTO.setBetsCount( matchBetsService.betsCount( match ) );

				if ( matchBet == null ) {
					return matchBetDTO;
				}

				final BetDTO betDTO = transformMatchBet( matchBet, user );

				matchBetDTO.setBet( betDTO );
				matchBetDTO.setPoints( cupScoresService.getUsersScores( matchBet ) );

				return matchBetDTO;
			}
		};
	}

	private Function<CupTeamBet, CupTeamBetDTO> cupTeamBetFunction( final User user ) {

		return new Function<CupTeamBet, CupTeamBetDTO>() {
			@Override
			public CupTeamBetDTO apply( final CupTeamBet cupTeamBet ) {

				final CupTeamBetDTO result = new CupTeamBetDTO();

				result.setCup( transformCup( cupTeamBet.getCup(), user ) );
				result.setTeam( transformTeam( cupTeamBet.getTeam() ) );
				result.setUser( transformUser( cupTeamBet.getUser() ) );

				result.setCupPosition( cupTeamBet.getCupPosition() );

				return result;
			}
		};
	}
}
