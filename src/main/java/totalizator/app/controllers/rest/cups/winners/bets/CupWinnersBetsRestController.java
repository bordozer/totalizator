package totalizator.app.controllers.rest.cups.winners.bets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.UserDTO;
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
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
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

		final Map<UserDTO, List<CupTeamBetDTO>> data = newLinkedHashMap();

		final List<UserDTO> users = Lists.transform( bets, new Function<CupTeamBetDTO, UserDTO>() {
			@Override
			public UserDTO apply( final CupTeamBetDTO cupBet ) {
				return null;
			}
		} );
		for ( final UserDTO user : users ) {

			final List<CupTeamBetDTO> userBets = newArrayList( bets );

			CollectionUtils.filter( userBets, new Predicate<CupTeamBetDTO>() {
				@Override
				public boolean evaluate( final CupTeamBetDTO cupTeamBetDTO ) {
					return cupTeamBetDTO.getUser().getUserId() == user.getUserId();
				}
			} );

			Collections.sort( userBets, new Comparator<CupTeamBetDTO>() {
				@Override
				public int compare( final CupTeamBetDTO o1, final CupTeamBetDTO o2 ) {
					return ( ( Integer ) o1.getCupPosition() ).compareTo( o2.getCupPosition() );
				}
			} );

			data.put( user, userBets );
		}

		result.setUsersCupBets( data );

		return result;
	}
}
