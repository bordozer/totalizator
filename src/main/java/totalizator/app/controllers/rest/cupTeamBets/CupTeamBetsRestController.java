package totalizator.app.controllers.rest.cupTeamBets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.CupTeamBetService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/cups/{cupId}/bets")
public class CupTeamBetsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private  CupTeamBetService cupTeamBetService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/", produces = APPLICATION_JSON_VALUE )
	public CupTeamBetsDTO cupTeamBets( final @PathVariable( "cupId" ) int cupId, final @PathVariable( "userId" ) int userId ) {

		final User user = userService.load( userId );
		final Cup cup = cupService.load( cupId );

		return new CupTeamBetsDTO( dtoService.transformCupTeamBets( cupTeamBetService.load( cup, user ) ) );
	}
}
