package totalizator.app.controllers.rest.admin.cups;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
	public List<CupDTO> cupsWithImportStrategies( final @RequestParam( "filteredBySportKindId" ) int filteredBySportKindId, final Principal principal ) {

		return dtoService.transformCups(
				allCups()
				.stream()
				.filter( getImportStrategiesPredicate() )
				.filter( getSportKindPredicate( filteredBySportKindId ) )
				.sorted( cupService.categoryNameOrCupNameComparator() )
				.collect( Collectors.toList() ), getUser( principal ) );
	}

	@RequestMapping(method = RequestMethod.GET, value = "/configured-for-remote-games-import/current/" )
	public List<CupDTO> currentCupsOnlyWithImportStrategies( final @RequestParam( "filteredBySportKindId" ) int filteredBySportKindId, final Principal principal ) {

		return dtoService.transformCups( currentCupsOnly()
				.stream()
				.filter( getImportStrategiesPredicate() )
				.filter( getSportKindPredicate( filteredBySportKindId ) )
				.sorted( cupService.categoryNameOrCupNameComparator() )
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

				final GameImportStrategyType strategyType = GameImportStrategyType.getById( cup.getCategory().getRemoteGameImportStrategyTypeId() );

				if ( GameImportStrategyType.CUP_ID_NEEDED.contains( strategyType ) && StringUtils.isEmpty( cup.getCupImportId() ) ) {
					return false;
				}

				return true;
			}
		};
	}

	private Predicate<Cup> getSportKindPredicate( final int filteredBySportKindId ) {

		return new Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {
				return cup.getCategory().getSportKind().getId() == filteredBySportKindId;
			}
		};
	}

	private User getUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
