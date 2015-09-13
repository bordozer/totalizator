package totalizator.app.controllers.rest.admin.cups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.services.CategoryService;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;

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

	@RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/" )
	public List<CupDTO> cupsWithImportStrategies( final Principal principal ) {

		return allCups( principal )
				.stream()
				.filter( getImportStrategiesPredicate() )
				.collect( Collectors.toList() );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/current/" )
	public List<CupDTO> currentCupsOnlyWithImportStrategies( final Principal principal ) {

		return currentCupsOnly( principal )
				.stream()
				.filter( getImportStrategiesPredicate() )
				.collect( Collectors.toList() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/" )
	public CupDTO getCup( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {
		return dtoService.transformCup( cupService.load( cupId ), userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/for-category/{categoryId}/" )
	public List<CupDTO> getCategoryCups( final @PathVariable( "categoryId" ) int categoryId, final Principal principal ) {
		return dtoService.transformCups( cupService.loadPublic( categoryService.load( categoryId ) ), userService.findByLogin( principal.getName() ) );
	}

	private Predicate<CupDTO> getImportStrategiesPredicate() {

		return new Predicate<CupDTO>() {
			@Override
			public boolean test( final CupDTO cupDTO ) {
				final Category category = categoryService.load( cupDTO.getCategory().getCategoryId() );
				return category.getRemoteGameImportStrategyTypeId() != 0; // && StringUtils.isNoneEmpty( category.getImportId() );
			}
		};
	}
}
