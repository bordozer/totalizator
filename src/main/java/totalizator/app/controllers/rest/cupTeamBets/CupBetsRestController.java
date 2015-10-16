package totalizator.app.controllers.rest.cupTeamBets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.*;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/bets" )
public class CupBetsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private DateTimeService dateTimeService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public CupTeamBetsDTO show( final @PathVariable( "cupId" ) int cupId, final @PathVariable( "userId" ) int userId ) {
		return getCupTeamBetsDTO( cupId, userId );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{position}/{teamId}/", produces = APPLICATION_JSON_VALUE )
	public void save( final @PathVariable( "cupId" ) int cupId, final @PathVariable( "position" ) int position, final @PathVariable( "teamId" ) int teamId, final Principal principal ) {

		// TODO: server validation

		final Cup cup = cupService.load( cupId );
		if ( cupBetsService.isCupBettingFinished( cup ) ) {
			throw new IllegalArgumentException( String.format( "Match betting for cup %s is finished", cup ) );
		}

		final User user = userService.findByLogin( principal.getName() );

		final CupTeamBet existingTupTeamBet = cupBetsService.load( cup, user, position );
		if ( cupId > 0 && position > 0 && teamId == 0 && existingTupTeamBet != null ) {
			cupBetsService.delete( existingTupTeamBet.getId() );
			return;
		}

		if ( cupId == 0 || teamId == 0 || position <= 0 ) {
			return; // no error, just return
		}

		final Team team = teamService.load( teamId );

		if ( existingTupTeamBet != null ) {
			existingTupTeamBet.setTeam( team );
			cupBetsService.save( existingTupTeamBet );

			return;
		}

		final CupTeamBet entry = new CupTeamBet();

		entry.setCup( cup );
		entry.setUser( user );
		entry.setTeam( team );
		entry.setCupPosition( position );

		cupBetsService.save( entry );
	}

	private CupTeamBetsDTO getCupTeamBetsDTO( final int cupId, final int userId ) {

		final User user = userService.load( userId );
		final Cup cup = cupService.load( cupId );

		final UserDTO userDTO = dtoService.transformUser( user );
		final CupDTO cupDTO = dtoService.transformCup( cup, user );

		final List<CupTeamBetDTO> result = newArrayList();

		for ( int i = 1; i <= cup.getWinnersCount(); i++ ) {

			final CupTeamBet cupTeamBet = cupBetsService.load( cup, user, i );

			if ( cupTeamBet != null ) {
				result.add( dtoService.transformCupTeamBet( cupTeamBet, user ) );
			} else {
				final CupTeamBetDTO emptyCupTeamBetDTO = new CupTeamBetDTO();
				emptyCupTeamBetDTO.setCup( cupDTO );
				emptyCupTeamBetDTO.setUser( userDTO );
				emptyCupTeamBetDTO.setCupPosition( i );

				result.add( emptyCupTeamBetDTO );
			}
		}

		return new CupTeamBetsDTO( result );
	}
}
