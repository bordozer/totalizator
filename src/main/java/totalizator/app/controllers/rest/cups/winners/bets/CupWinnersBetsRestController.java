package totalizator.app.controllers.rest.cups.winners.bets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.services.CupBetsService;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/winners/bets" )
public class CupWinnersBetsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public CupWinnersBetsDTO all( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final Cup cup = cupService.load( cupId );
		final List<CupTeamBet> cupBets = cupBetsService.load( cup );

		final CupWinnersBetsDTO result = new CupWinnersBetsDTO();
		result.setWinnersCount( cup.getWinnersCount() );

		final List<CupTeamBetDTO> bets = dtoService.transformCupTeamBets( cupBets, userService.findByLogin( principal.getName() ) );
		Collections.sort( bets, new Comparator<CupTeamBetDTO>() {
			@Override
			public int compare( final CupTeamBetDTO o1, final CupTeamBetDTO o2 ) {

				if ( o1.getUser().getUserId() != o2.getUser().getUserId() ) {
					return o1.getUser().getUserName().compareTo( o2.getUser().getUserName() );
				}

				return o1.getCupPosition() - o2.getCupPosition();
			}
		} );

		result.setCupBets( bets );

		return result;
	}
}
