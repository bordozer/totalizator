package totalizator.app.controllers.rest.admin.cups;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CategoryService;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.imports.GameImportStrategyType;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/admin/rest/cups")
public class AdminCupsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping(method = RequestMethod.GET, value = "/" )
	public List<CupDTO> allCupsDTOs( final Principal principal ) {
		return dtoService.transformCups( allCups(), getUser( principal ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/current/" )
	public List<CupDTO> currentCupsOnlyDTOs( final Principal principal ) {
		return dtoService.transformCups( currentCupsOnly(), getUser( principal ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/" )
	public List<CupDTO> cupsWithImportStrategies( final Principal principal ) {

		return dtoService.transformCups( allCups()
				.stream()
				.filter( getImportStrategiesPredicate() )
				.collect( Collectors.toList() ), getUser( principal ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/current/" )
	public List<CupDTO> currentCupsOnlyWithImportStrategies( final Principal principal ) {

		return dtoService.transformCups( currentCupsOnly()
				.stream()
				.filter( getImportStrategiesPredicate() )
				.collect( Collectors.toList() ), getUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/" )
	public CupDTO getCup( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		return dtoService.transformCup( cupService.load( cupId ), getUser( principal ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/for-category/{categoryId}/" )
	public List<CupDTO> getCategoryCups( final @PathVariable( "categoryId" ) int categoryId, final Principal principal ) {
		return dtoService.transformCups( cupService.load( categoryService.load( categoryId ) ), getUser( principal ) );
	}

	private List<Cup> allCups() {

		final List<Cup> publicCups = cupService.loadPublic();
		final List<Cup> hiddenCups = cupService.loadHidden();

		final List<Cup> result = newArrayList();
		result.addAll( publicCups );
		result.addAll( hiddenCups );

		return result;
	}

	private List<Cup> currentCupsOnly() {

		final List<Cup> publicCurrentCups = cupService.loadPublicCurrent();
		final List<Cup> nonPublicCurrentCups = cupService.loadHiddenCurrent();

		final List<Cup> result = newArrayList();
		result.addAll( publicCurrentCups );
		result.addAll( nonPublicCurrentCups );

		return result;
	}

	// TODO: mode to service
	// TODO: cup on save validation
	// TODO: TEST!!!
	private Predicate<Cup> getImportStrategiesPredicate() {

		return new Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {

				final int strategyTypeId = cup.getCategory().getRemoteGameImportStrategyTypeId();

				return strategyTypeId == GameImportStrategyType.NBA.getId()
						|| ( strategyTypeId == GameImportStrategyType.UEFA.getId() && StringUtils.isNotEmpty( cup.getCupImportId() ) );
			}
		};
	}

	private User getUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
