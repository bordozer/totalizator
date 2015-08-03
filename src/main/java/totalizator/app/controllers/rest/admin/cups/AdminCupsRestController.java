package totalizator.app.controllers.rest.admin.cups;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.services.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Iterator;
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
	private TeamService teamService;

	@Autowired
	private LogoService logoService;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Autowired
	private UserService userService;

	@Autowired
	private PointsCalculationStrategyService pointsCalculationStrategyService;

	@Autowired
	private DTOService dtoService;

	private static final Logger LOGGER = Logger.getLogger( AdminCupsRestController.class );

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/all/", produces = APPLICATION_JSON_VALUE)
	public List<CupDTO> entries( final Principal principal ) {
		return dtoService.transformCups( cupService.loadAll(), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public CupDTO getCup( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		return dtoService.transformCup( cupService.load( cupId ), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE)
	public List<CupEditDTO> entries() {
		return Lists.transform( cupService.loadAll(), getFunction() );
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public CupEditDTO create( final @RequestBody CupEditDTO cupEditDTO ) {
		// TODO: check if name exists

		final Cup cup = new Cup();

		initFromDTO( cup, cupEditDTO );

		final Cup saved = cupService.save( cup );

		return getFunction().apply( saved );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{cupId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupEditDTO edit( final @PathVariable( "cupId" ) int cupId, final @RequestBody CupEditDTO cupEditDTO ) {
		// TODO: check if name exists

		final Cup cup = cupService.load( cupEditDTO.getCupId() );

		initFromDTO( cup, cupEditDTO );

		final List<CupWinner> winners = Lists.transform( cupEditDTO.getCupWinners(), new Function<CupWinnerEditDTO, CupWinner>() {

			@Override
			public CupWinner apply( final CupWinnerEditDTO dto ) {
				final CupWinner winner = new CupWinner();
				winner.setCup( cup );
				winner.setCupPosition( dto.getCupPosition() );
				winner.setTeam( teamService.load( dto.getTeamId() ) );

				return winner;
			}
		} );

		final Cup saved = cupService.save( cup, winners );

		return getFunction().apply( saved );
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

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{cupId}/logo/" )
	public void uploadLogo( final @PathVariable( "cupId" ) int cupId, final MultipartHttpServletRequest request ) throws IOException {

		final Cup cup = cupService.load( cupId );

		final Iterator<String> itr = request.getFileNames();
		if ( ! itr.hasNext() ) {
			return;
		}

		final MultipartFile logoFile = request.getFile( itr.next() );

		cup.setLogoFileName( String.format( "cup_logo_%d", cup.getId() ) );
		cupService.save( cup );

		logoFile.transferTo( logoService.getLogoFile( cup ) );
	}

	private void initFromDTO( final Cup cup, final CupEditDTO dto ) {

		cup.setCupName( dto.getCupName() );
		cup.setCategory( categoryService.load( dto.getCategoryId() ) );

		cup.setCupStartTime( dto.getCupStartDate() );
		cup.setWinnersCount( dto.getWinnersCount() );

		cup.setPublicCup( dto.isPublicCup() );

		cup.setPointsCalculationStrategy( pointsCalculationStrategyService.load( dto.getCupPointsCalculationStrategyId() ) );
	}

	private List<CupWinnerEditDTO> getCupWinners( final Cup cup ) {

		return Lists.transform( cupWinnerService.loadAll( cup ), new Function<CupWinner, CupWinnerEditDTO>() {
			@Override
			public CupWinnerEditDTO apply( final CupWinner cupWinner ) {
				return new CupWinnerEditDTO( cupWinner.getCup().getId(), cupWinner.getCupPosition(), cupWinner.getTeam().getId() );
			}
		} );
	}

	private Function<Cup, CupEditDTO> getFunction() {

		return new Function<Cup, CupEditDTO>() {

			@Override
			public CupEditDTO apply( final Cup cup ) {

				final CupEditDTO cupEditDTO = new CupEditDTO();

				cupEditDTO.setCupId( cup.getId() );
				cupEditDTO.setCupName( cup.getCupName() );
				cupEditDTO.setCategoryId( cup.getCategory().getId() );


				cupEditDTO.setPublicCup( cup.isPublicCup() );
				cupEditDTO.setWinnersCount( cup.getWinnersCount() );
				cupEditDTO.setCupStartDate( cup.getCupStartTime() );
				cupEditDTO.setLogoUrl( logoService.getLogoURL( cup ) );
				cupEditDTO.setCupWinners( getCupWinners( cup ) );

				cupEditDTO.setReadyForCupBets( !cupBetsService.isCupBettingFinished( cup ) );
				cupEditDTO.setReadyForMatchBets( !cupBetsService.isMatchBettingFinished( cup ) );

				cupEditDTO.setFinished( cupService.isCupFinished( cup ) );
				cupEditDTO.setCupPointsCalculationStrategyId( cup.getPointsCalculationStrategy().getId() );

				return cupEditDTO;
			}
		};
	}
}
