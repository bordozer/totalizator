package totalizator.app.controllers.rest.admin.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.matches.MatchesAndBetsWidgetService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping( "/admin/rest/matches" )
public class AdminMatchesRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchesAndBetsWidgetService matchesAndBetsWidgetService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchEditDTO> entries( final MatchesBetSettingsDTO dto, final Principal principal ) {

		final User currentUser = getCurrentUser( principal );

		final List<Match> matches = matchesAndBetsWidgetService.loadAll( dto );

		return Lists.transform( matches, new Function<Match, MatchEditDTO>() {

			@Override
			public MatchEditDTO apply( final Match match ) {

				final MatchEditDTO matchEditDTO = new MatchEditDTO();

				matchEditDTO.setMatchId( match.getId() );
				matchEditDTO.setCategoryId( match.getCup().getCategory().getId() );
				matchEditDTO.setCupId( match.getCup().getId() );

				matchEditDTO.setTeam1Id( match.getTeam1().getId() );
				matchEditDTO.setScore1( match.getScore1() );

				matchEditDTO.setTeam2Id( match.getTeam2().getId() );
				matchEditDTO.setScore2( match.getScore2() );

				matchEditDTO.setBeginningTime( match.getBeginningTime() );
				matchEditDTO.setMatchFinished( match.isMatchFinished() );

				matchEditDTO.setHomeTeamNumber( match.getHomeTeamNumber() );
				matchEditDTO.setMatchDescription( match.getDescription() );

				matchEditDTO.setBetsCount( matchBetsService.betsCount( match ) );

				matchEditDTO.setRemoteGameId( match.getRemoteGameId() );

				initReadOnlyDTOProperties( matchEditDTO, match, currentUser );

				return matchEditDTO;
			}
		} );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchEditDTO create( final @RequestBody MatchEditDTO matchEditDTO, final Principal principal ) {

		final User currentUser = getCurrentUser( principal );

		final Match match = new Match();

		initMatchFromDTO( matchEditDTO, match );

		final Match saved = matchService.save( match );

		matchEditDTO.setMatchId( saved.getId() );

		initReadOnlyDTOProperties( matchEditDTO, match, currentUser );

		return matchEditDTO;
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchEditDTO save( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchEditDTO matchEditDTO, final Principal principal ) {

		final User currentUser = getCurrentUser( principal );

		final Match match = matchService.load( matchEditDTO.getMatchId() );

		initMatchFromDTO( matchEditDTO, match );

		matchService.save( match );

		initReadOnlyDTOProperties( matchEditDTO, match, currentUser );

		return matchEditDTO;
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchId}" )
	public void delete( final @PathVariable( "matchId" ) int matchId ) {

		if ( matchId == 0 ) {
			return;
		}

		matchService.delete( matchId );
	}

	private void initReadOnlyDTOProperties( final @RequestBody MatchEditDTO dto, final Match match, final User currentUser ) {
		dto.setTeam1( dtoService.transformTeam( match.getTeam1(), currentUser ) );
		dto.setTeam2( dtoService.transformTeam( match.getTeam2(), currentUser ) );
	}

	private void initMatchFromDTO( final MatchEditDTO matchEditDTO, final Match match ) {

		match.setCup( cupService.load( matchEditDTO.getCupId() ) );

		match.setTeam1( teamService.load( matchEditDTO.getTeam1Id() ) );
		match.setScore1( matchEditDTO.getScore1() );

		match.setTeam2( teamService.load( matchEditDTO.getTeam2Id() ) );
		match.setScore2( matchEditDTO.getScore2() );

		match.setBeginningTime( matchEditDTO.getBeginningTime() );

		match.setMatchFinished( matchEditDTO.isMatchFinished() );

		match.setHomeTeamNumber( matchEditDTO.getHomeTeamNumber() );
		match.setDescription( matchEditDTO.getMatchDescription() );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
