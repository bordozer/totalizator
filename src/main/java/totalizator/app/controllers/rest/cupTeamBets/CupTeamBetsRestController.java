package totalizator.app.controllers.rest.cupTeamBets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.enums.CupPosition;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/bets" )
public class CupTeamBetsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupTeamBetService cupTeamBetService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public CupTeamBetsDTO show( final @PathVariable( "cupId" ) int cupId, final @PathVariable( "userId" ) int userId ) {
		return getCupTeamBetsDTO( cupId, userId );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public CupTeamBetsDTO save( final List<UserCupTeamBetsDTO> userCupTeamBetsDTOs, final @PathVariable( "cupId" ) int cupId, final @PathVariable( "userId" ) int userId ) {

		final User user = userService.load( userId );
		final Cup cup = cupService.load( cupId );

		for ( final UserCupTeamBetsDTO userCupTeamBetsDTO : userCupTeamBetsDTOs ) {

			final CupTeamBet entry = new CupTeamBet();
			entry.setCup( cup );
			entry.setUser( user );

			entry.setTeam( teamService.load( userCupTeamBetsDTO.getTeamId() ) );
			entry.setCupPosition( CupPosition.getById( userCupTeamBetsDTO.getCupPositionId() ) );

			entry.setBetTime( new Date() );

			cupTeamBetService.save( entry );
		}

		return getCupTeamBetsDTO( cupId, userId );
	}

	private CupTeamBetsDTO getCupTeamBetsDTO( final int cupId, final int userId ) {

		final User user = userService.load( userId );
		final Cup cup = cupService.load( cupId );

		final UserDTO userDTO = dtoService.transformUser( user );
		final CupDTO cupDTO = dtoService.transformCup( cup );

		final List<CupTeamBetDTO> result = newArrayList();

		for ( final CupPosition cupPosition : CupPosition.values() ) {

			final CupTeamBet cupTeamBet = cupTeamBetService.load( cup, user, cupPosition );

			if ( cupTeamBet != null ) {
				final CupTeamBetDTO cupTeamBetDTO = dtoService.transformCupTeamBet( cupTeamBet );
				result.add( cupTeamBetDTO );
			} else {
				final CupTeamBetDTO emptyCupTeamBetDTO = new CupTeamBetDTO();
				emptyCupTeamBetDTO.setCup( cupDTO );
				emptyCupTeamBetDTO.setUser( userDTO );
				emptyCupTeamBetDTO.setCupPosition( dtoService.transformCupPosition( cupPosition ) );

				result.add( emptyCupTeamBetDTO );
			}
		}

		return new CupTeamBetsDTO( result );
	}
}
