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
	public List<MatchDTOAdmin> entries( final MatchesBetSettingsDTO dto, final Principal principal ) {

		final List<Match> matches = matchService.loadAll( dto );

		return Lists.transform( matches, new Function<Match, MatchDTOAdmin>() {
			@Override
			public MatchDTOAdmin apply( final Match match ) {

				final MatchDTOAdmin matchDTOAdmin = new MatchDTOAdmin();

				matchDTOAdmin.setMatchId( match.getId() );
				matchDTOAdmin.setCategoryId( match.getCup().getCategory().getId() );
				matchDTOAdmin.setCupId( match.getCup().getId() );

				matchDTOAdmin.setTeam1Id( match.getTeam1().getId() );
				matchDTOAdmin.setScore1( match.getScore1() );

				matchDTOAdmin.setTeam2Id( match.getTeam2().getId() );
				matchDTOAdmin.setScore2( match.getScore2() );

				matchDTOAdmin.setBeginningTime( match.getBeginningTime() );
				matchDTOAdmin.setMatchFinished( match.isMatchFinished() );

				return matchDTOAdmin;
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTOAdmin create( final @RequestBody MatchDTOAdmin matchDTOAdmin ) {

		final Match match = new Match();

		initMatchFromDTO( matchDTOAdmin, match );

		final Match saved = matchService.save( match );

		matchDTOAdmin.setMatchId( saved.getId() );

		return matchDTOAdmin;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTOAdmin save( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchDTOAdmin matchDTOAdmin, final Principal principal ) {

		final Match match = matchService.load( matchDTOAdmin.getMatchId() );

		initMatchFromDTO( matchDTOAdmin, match );

		matchService.save( match );

		return matchDTOAdmin;
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

	private void initMatchFromDTO( final MatchDTOAdmin matchDTOAdmin, final Match match ) {

		match.setCup( cupService.load( matchDTOAdmin.getCupId() ) );

		match.setTeam1( teamService.load( matchDTOAdmin.getTeam1Id() ) );
		match.setScore1( matchDTOAdmin.getScore1() );

		match.setTeam2( teamService.load( matchDTOAdmin.getTeam2Id() ) );
		match.setScore2( matchDTOAdmin.getScore2() );

		match.setBeginningTime( matchDTOAdmin.getBeginningTime() );

		match.setMatchFinished( matchDTOAdmin.isMatchFinished() );
	}

}
