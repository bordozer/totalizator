package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.*;
import totalizator.app.models.*;
import totalizator.app.services.score.CupScoresService;

import java.util.List;

@Service
public class DTOServiceImpl implements DTOService {

	@Autowired
	private TeamLogoService teamLogoService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupScoresService cupScoresService;

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

	@Override
	public MatchDTO transformMatch( final Match match ) {
		return matchFunction().apply( match );
	}

	@Override
	public List<MatchDTO> transformMatches( final List<Match> matches ) {
		return Lists.transform( matches, matchFunction() );
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
	public void initMatchFromDTO( final MatchDTO matchDTO, final Match match ) {
		match.setCup( cupService.load( matchDTO.getCupId() ) );

		match.setTeam1( teamService.load( matchDTO.getTeam1().getTeamId() ) );
		match.setScore1( matchDTO.getScore1() );

		match.setTeam2( teamService.load( matchDTO.getTeam2().getTeamId() ) );
		match.setScore2( matchDTO.getScore2() );

		match.setBeginningTime( matchDTO.getBeginningTime() );

		match.setMatchFinished( matchDTO.isMatchFinished() );
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
				return new CategoryDTO( category.getId(), category.getCategoryName() );
			}
		};
	}

	private Function<Cup, CupDTO> cupFunction() {

		return new Function<Cup, CupDTO>() {

			@Override
			public CupDTO apply( final Cup cup ) {
				final CupDTO cupDTO = new CupDTO( cup.getId(), cup.getCupName(), transformCategory( cup.getCategory() ) );
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

	private Function<Match, MatchDTO> matchFunction() {

		return new Function<Match, MatchDTO>() {

			@Override
			public MatchDTO apply( final Match match ) {
				final MatchDTO dto = new MatchDTO();

				dto.setMatchId( match.getId() );
				dto.setCategoryId( match.getCup().getCategory().getId() );
				dto.setCupId( match.getCup().getId() );

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

				final MatchDTO matchDTO = transformMatch( matchBet.getMatch() );

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
				final MatchDTO matchDTO = transformMatch( match );

				final MatchBetDTO matchBetDTO = new MatchBetDTO( matchDTO );
				matchBetDTO.setBettingAllowed( matchBetsService.isBettingAllowed( match, user ) );

				final MatchBet matchBet = matchBetsService.load( user, match );

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
}
