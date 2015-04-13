package totalizator.app.controllers.rest.admin.cups;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupWinnerDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin/rest/cups")
public class AdminCupsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private LogoService logoService;

	@Autowired
	private CupWinnerService cupWinnerService;

	private static final Logger LOGGER = Logger.getLogger( AdminCupsRestController.class );

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
	public List<CupEditDTO> entries( final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		return Lists.transform( cupService.loadAll(), new Function<Cup, CupEditDTO>() {
			@Override
			public CupEditDTO apply( final Cup cup ) {

				final CupEditDTO cupEditDTO = new CupEditDTO();

				cupEditDTO.setCupId( cup.getId() );
				cupEditDTO.setCupName( cup.getCupName() );
				cupEditDTO.setCategoryId( cup.getCategory().getId() );


				cupEditDTO.setShowOnPortalPage( cup.isShowOnPortalPage() );
				cupEditDTO.setWinnersCount( cup.getWinnersCount() );
				cupEditDTO.setCupStartDate( cup.getCupStartTime() );
				cupEditDTO.setReadyForCupBets( cup.isReadyForCupBets() );
				cupEditDTO.setReadyForMatchBets( cup.isReadyForMatchBets() );
				cupEditDTO.setCupBettingIsAllowed( cupBetsService.isNotTooLateForCupBetting( cup, currentUser ) );
				cupEditDTO.setFinished( cup.isFinished() );
				cupEditDTO.setLogoUrl( logoService.getLogoURL( cup ) );
				cupEditDTO.setCupWinners( getCupWinners( cup ) );

				return cupEditDTO;
			}
		} );
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public CupEditDTO create( final @RequestBody CupEditDTO cupEditDTO ) {
		// TODO: check if name exists

		final Cup cup = new Cup();

		initFromDTO( cupEditDTO, cup );

		cupService.save( cup );

		cupEditDTO.setCupId( cup.getId() );

		return cupEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{cupId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupEditDTO edit( final @PathVariable( "cupId" ) int cupId, final @RequestBody CupEditDTO cupEditDTO ) {
		// TODO: check if name exists

		final Cup cup = cupService.load( cupEditDTO.getCupId() );

		initFromDTO( cupEditDTO, cup );

		final Cup save = cupService.save( cup );

		final List<CupWinnerDTO> cupWinners = cupEditDTO.getCupWinners();
		for ( final CupWinnerDTO cupWinner : cupWinners ) {

			final CupWinner winner = new CupWinner();
			winner.setCup( save );
			winner.setCupPosition( cupWinner.getCupPosition() );
			winner.setTeam( teamService.load( cupWinner.getTeamId() ) );

			cupWinnerService.save( winner );
		}

		return cupEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{cupId}" )
	public void delete( final @PathVariable( "cupId" ) int cupId ) {

		if ( cupId == 0 ) {
			return;
		}

		cupService.delete( cupId );
	}

	private void initFromDTO( final CupEditDTO cupEditDTO, final Cup cup ) {

		cup.setCupName( cupEditDTO.getCupName() );
		cup.setCategory( categoryService.load( cupEditDTO.getCategoryId() ) );

		cup.setCupStartTime( cupEditDTO.getCupStartDate() );
		cup.setWinnersCount( cupEditDTO.getWinnersCount() );
		cup.setFinished( cupEditDTO.isFinished() );

		cup.setReadyForCupBets( cupEditDTO.isReadyForCupBets() );
		cup.setReadyForMatchBets( cupEditDTO.isReadyForMatchBets() );

		cup.setShowOnPortalPage( cupEditDTO.isShowOnPortalPage() );
	}

	private List<CupWinnerDTO> getCupWinners( final Cup cup ) {

		return Lists.transform( cupWinnerService.loadAll( cup ), new Function<CupWinner, CupWinnerDTO>() {
			@Override
			public CupWinnerDTO apply( final CupWinner cupWinner ) {
				return new CupWinnerDTO( cupWinner.getCup().getId(), cupWinner.getCupPosition(), cupWinner.getTeam().getId() );
			}
		} );
	}
}
