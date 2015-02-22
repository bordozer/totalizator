package totalizator.app.controllers.rest.admin.cups;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/cup" )
public class CupController {

	@Autowired
	private CupService cupService;

	private static final Logger LOGGER = Logger.getLogger( CupController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> entries() {

		final List<CupDTO> result = newArrayList();

		for ( final Cup cup : cupService.loadAll() ) {
			result.add( new CupDTO( cup.getId(), cup.getCupName() ) );
		}

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupDTO create( final @RequestBody CupDTO cupDTO ) {
		// TODO: check if name exists
		cupService.save( new Cup( cupDTO.getCupName() ) );

		return cupDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{cupId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupDTO edit( final @PathVariable( "cupId" ) int cupId, final @RequestBody CupDTO cupDTO ) {
		// TODO: check if name exists
		final Cup cup = cupService.load( cupDTO.getCupId() );
		cup.setCupName( cupDTO.getCupName() );

		cupService.save( cup );

		return cupDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{cupId}" )
	public void delete( final @PathVariable( "cupId" ) int cupId ) {
		cupService.delete( cupId );
	}
}
