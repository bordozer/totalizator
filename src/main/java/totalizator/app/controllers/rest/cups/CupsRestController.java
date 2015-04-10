package totalizator.app.controllers.rest.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
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
	private DTOService dtoService;

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public CupDTO getDefaultLogin( final Principal principal, final @PathVariable( "cupId" ) int cupId ) {
		return dtoService.transformCup( cupService.load( cupId ), userService.findByLogin( principal.getName() ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/navi/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> cupsToShow( final Principal principal ) {
		return dtoService.transformCups( cupService.portalPageCups(), userService.findByLogin( principal.getName() ) );
	}
}
