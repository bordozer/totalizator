package totalizator.app.controllers.rest.admin.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/matches" )
public class AdminMatchesRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchEditDTO> entries( final MatchesBetSettingsDTO dto, final Principal principal ) {

		final List<Match> matches = matchService.loadAll( dto );

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

				return matchEditDTO;
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchEditDTO create( final @RequestBody MatchEditDTO matchEditDTO ) {

		final Match match = new Match();

		initMatchFromDTO( matchEditDTO, match );

		final Match saved = matchService.save( match );

		matchEditDTO.setMatchId( saved.getId() );

		return matchEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchEditDTO save( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchEditDTO matchEditDTO, final Principal principal ) {

		final Match match = matchService.load( matchEditDTO.getMatchId() );

		initMatchFromDTO( matchEditDTO, match );

		matchService.save( match );

		return matchEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchId}" )
	public void delete( final @PathVariable( "matchId" ) int matchId ) {

		if ( matchId == 0 ) {
			return;
		}

		matchService.delete( matchId );
	}

	private void initMatchFromDTO( final MatchEditDTO matchEditDTO, final Match match ) {

		match.setCup( cupService.load( matchEditDTO.getCupId() ) );

		match.setTeam1( teamService.load( matchEditDTO.getTeam1Id() ) );
		match.setScore1( matchEditDTO.getScore1() );

		match.setTeam2( teamService.load( matchEditDTO.getTeam2Id() ) );
		match.setScore2( matchEditDTO.getScore2() );

		match.setBeginningTime( matchEditDTO.getBeginningTime() );

		match.setMatchFinished( matchEditDTO.isMatchFinished() );
	}

}
