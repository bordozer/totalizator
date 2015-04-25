package totalizator.app.controllers.rest.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups" )
public class CupsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> all( final Principal principal ) {
		return publicCups( principal );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public CupDTO getDefaultLogin( final Principal principal, final @PathVariable( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );

		if ( ! cup.isPublicCup() ) {
			throw new IllegalStateException( String.format( "Cup %s is not public", cup ) );
		}

		return dtoService.transformCup( cup, userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/public/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> publicCups( final Principal principal ) {
		return dtoService.transformCups( cupService.loadAllPublic(), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/current/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> currentCups( final Principal principal ) {
		return dtoService.transformCups( cupService.loadAllCurrent(), userService.findByLogin( principal.getName() ) );
	}
}
