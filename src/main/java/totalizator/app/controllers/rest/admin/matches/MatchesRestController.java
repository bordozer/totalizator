package totalizator.app.controllers.rest.admin.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;
import totalizator.app.services.MatchService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/matches" )
public class MatchesRestController {

	@Autowired
	private MatchService matchService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> entries() {

		return Lists.transform( matchService.loadAll(), new Function<Match, MatchDTO>() {
			@Override
			public MatchDTO apply( final Match match ) {
				return matchService.initDTOFromModel( match );
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO create( final @RequestBody MatchDTO matchDTO ) {

		final Match match = new Match();

		matchService.initModelFromDTO( matchDTO, match );

		final Match saved = matchService.save( match );

		matchDTO.setMatchId( saved.getId() );

		return matchDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{matchId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public MatchDTO edit( final @PathVariable( "matchId" ) int matchId, final @RequestBody MatchDTO matchDTO ) {

		final Match match = matchService.load( matchDTO.getCupId() );

		matchService.initModelFromDTO( matchDTO, match );

		matchService.save( match );

		return matchService.initDTOFromModel( match );
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
}
