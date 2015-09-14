package totalizator.app.controllers.rest.sportKindCups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.SportKindDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.SportKind;
import totalizator.app.models.User;
import totalizator.app.services.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping( "/rest/sport-kinds/" )
public class NavigationTabsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SportKindService sportKindService;

	@Autowired
	private CupService cupService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/cups/active/" )
	public SportKindsCupsDTO activeCupsForNavigationTabs( final Principal principal ) {
		return getSportKindCupsDTO( principal, cupService.loadPublicCurrent() );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{sportKindId}/cups/" )
	public List<CategoryCupsDTO> all( final @PathVariable( "sportKindId" ) int sportKindId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final SportKind sportKind = sportKindService.load( sportKindId );

		final List<Category> categories = categoryService.loadAll( sportKindId );
		return categories
				.stream()
				.map( new Function<Category, CategoryCupsDTO>() {

					@Override
					public CategoryCupsDTO apply( final Category category ) {

						final CategoryCupsDTO dto = new CategoryCupsDTO();
						dto.setCategory( dtoService.transformCategory( category ) );
						dto.setCups( dtoService.transformCups( cupService.loadPublic( category ), currentUser ) );

						return dto;
					}
				} )
				.collect( Collectors.toList() );
	}

	private SportKindsCupsDTO getSportKindCupsDTO( final Principal principal, final List<Cup> cups ) {
		final User currentUser = userService.findByLogin( principal.getName() );

		final List<CupDTO> cupDTOs = dtoService.transformCups( cups, currentUser );

		final SportKindsCupsDTO dto = new SportKindsCupsDTO();

		final List<SportKindCupsDTO> list = newArrayList();
		for ( final SportKind sportKind : sportKindService.loadAll() ) {
			list.add( getSportKindCupsDTO( cupDTOs, sportKind ) );
		}

		dto.setSportKindCups( list );

		return dto;
	}

	private SportKindCupsDTO getSportKindCupsDTO( final List<CupDTO> cupDTOs, final SportKind sportKind ) {
		final SportKindDTO sportKindDTO = dtoService.transformSportKind( sportKind );

		final SportKindCupsDTO entry = new SportKindCupsDTO( sportKindDTO );
		entry.setCups( cupDTOs
						.stream()
						.filter( sportKindCupsMapper( sportKindDTO ) )
						.sorted( cupSorting() )
						.collect( Collectors.toList() )
		);
		return entry;
	}

	private Predicate<CupDTO> sportKindCupsMapper( final SportKindDTO sportKindDTO ) {

		return new Predicate<CupDTO>() {

			@Override
			public boolean test( final CupDTO cupDTO ) {
				return cupDTO.getCategory().getSportKind().getSportKindId() == sportKindDTO.getSportKindId();
			}
		};
	}

	private Comparator<CupDTO> cupSorting() {

		return new Comparator<CupDTO>() {

			@Override
			public int compare( final CupDTO o1, final CupDTO o2 ) {

				if ( o1.getCategory().equals( o2.getCategory() ) ) {
					return o1.getCupName().compareToIgnoreCase( o2.getCupName() );
				}

				return o1.getCategory().getCategoryName().compareToIgnoreCase( o2.getCategory().getCategoryName() );
			}
		};
	}
}
