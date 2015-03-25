package totalizator.app.controllers.rest.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups" )
public class CupMatchesAndBetsController {

	@Autowired
	private CupService cupService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public CupMatchesAndBetsDTO getDefaultLogin( final Principal principal, final @PathVariable( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );

		final CupMatchesAndBetsDTO result = new CupMatchesAndBetsDTO();

		final CupDTO dto = new CupDTO( cup.getId(), cup.getCupName(), cup.getCategory().getId() );
		result.setCup( dto );
		return result;
	}
}
