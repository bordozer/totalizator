package totalizator.app.controllers.rest.cups;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups" )
public class CupsRestController {

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

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/navi/", produces = APPLICATION_JSON_VALUE )
	public List<CupDTO> cupsToShow() {

		final List<Cup> portalPageCups = cupService.loadAll();
		CollectionUtils.filter( portalPageCups, new Predicate<Cup>() {
			@Override
			public boolean evaluate( final Cup cup ) {
				return cup.isShowOnPortalPage();
			}
		} );

		return Lists.transform( portalPageCups, new Function<Cup, CupDTO>() {
			@Override
			public CupDTO apply( final Cup cup ) {
				return new CupDTO( cup.getId(), cup.getCupName(), cup.getCategory().getId() );
			}
		} );
	}
}
