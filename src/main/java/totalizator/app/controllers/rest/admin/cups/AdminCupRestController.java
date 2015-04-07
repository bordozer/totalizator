package totalizator.app.controllers.rest.admin.cups;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CategoryService;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/cups" )
public class AdminCupRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DTOService dtoService;

	private static final Logger LOGGER = Logger.getLogger( AdminCupRestController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> entries() {
		return dtoService.transformCups( cupService.loadAll() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupDTO create( final @RequestBody CupDTO cupDTO ) {
		// TODO: check if name exists
		final Cup cup = new Cup( cupDTO.getCupName(), categoryService.load( cupDTO.getCategoryId() ) );
		cup.setShowOnPortalPage( cupDTO.isShowOnPortalPage() );

		cupService.save( cup );

		cupDTO.setCupId( cup.getId() );
		return cupDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{cupId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public CupDTO edit( final @PathVariable( "cupId" ) int cupId, final @RequestBody CupDTO cupDTO ) {
		// TODO: check if name exists
		final Cup cup = cupService.load( cupDTO.getCupId() );
		cup.setCupName( cupDTO.getCupName() );
		cup.setCategory( categoryService.load( cupDTO.getCategoryId() ) );
		cup.setShowOnPortalPage( cupDTO.isShowOnPortalPage() );

		cupService.save( cup );

		return cupDTO;
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
}
