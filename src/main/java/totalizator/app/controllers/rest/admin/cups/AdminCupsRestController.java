package totalizator.app.controllers.rest.admin.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/admin/rest/cups")
public class AdminCupsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping(method = RequestMethod.GET, value = "/" )
	public List<CupDTO> allCups( final Principal principal ) {

		final List<Cup> publicCups = cupService.loadPublic();
		final List<Cup> hiddenCups = cupService.loadHidden();

		final List<Cup> result = newArrayList();
		result.addAll( publicCups );
		result.addAll( hiddenCups );

		return dtoService.transformCups( result, userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/current/" )
	public List<CupDTO> currentCupsOnly( final Principal principal ) {

		final List<Cup> publicCurrentCups = cupService.loadPublicCurrent();
		final List<Cup> nonPublicCurrentCups = cupService.loadHiddenCurrent();

		final List<Cup> result = newArrayList();
		result.addAll( publicCurrentCups );
		result.addAll( nonPublicCurrentCups );

		return dtoService.transformCups( result, userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/" )
	public CupDTO getCup( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		return dtoService.transformCup( cupService.load( cupId ), userService.findByLogin( principal.getName() ) );
	}
}
