package totalizator.app.controllers.rest.admin.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/matches" )
public class MatchesRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> entries() {

		return Lists.transform( matchService.loadAll(), new Function<Match, MatchDTO>() {
			@Override
			public MatchDTO apply( final Match match ) {

				final MatchDTO dto = new MatchDTO();

				dto.setMatchId( match.getId() );
				dto.setCupId( match.getCup().getId() );

				dto.setTeam1Id( match.getTeam1().getId() );
				dto.setTeam1Name( match.getTeam1().getTeamName() );
				dto.setScore1Id( match.getScore1Id() );

				dto.setTeam2Id( match.getTeam2().getId() );
				dto.setTeam2Name( match.getTeam2().getTeamName() );
				dto.setScore2Id( match.getScore2Id() );

				dto.setLastBetTime( match.getLastBetTime() );

				return dto;
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO create( final @RequestBody MatchDTO matchDTO ) {
		// TODO: check if name exists

		final Match match = new Match();

		initFromDTO( matchDTO, match );

		matchService.save( match );

		matchDTO.setMatchId( match.getId() );

		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO edit( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchDTO matchDTO ) {
		// TODO: check if name exists
		final Match match = matchService.load( matchDTO.getCupId() );

		initFromDTO( matchDTO, match );

		matchService.save( match );

		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{matchId}" )
	public void delete( final @PathVariable( "matchId" ) int matchId ) {
		if ( matchId == 0 ) {
			return;
		}
		// delete
		matchService.delete( matchId );
	}

	private void initFromDTO( final MatchDTO matchDTO, final Match match ) {
		match.setCup( cupService.load( matchDTO.getCupId() ) );

		match.setTeam1( teamService.load( matchDTO.getTeam1Id() ) );
		match.setScore1Id( matchDTO.getScore1Id() );

		match.setTeam2( teamService.load( matchDTO.getTeam2Id() ) );
		match.setScore2Id( matchDTO.getScore2Id() );
		match.setLastBetTime( matchDTO.getLastBetTime() );
	}
}
