package totalizator.app.controllers.rest.sportKindCups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.beans.AppContext;
import totalizator.app.dto.SportKindDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.SportKind;
import totalizator.app.models.User;
import totalizator.app.services.*;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

@RestController
@RequestMapping( "/rest/sport-kinds/" )
public class SportKindCupsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SportKindService sportKindService;

	@Autowired
	private CupService cupService;

	@Autowired
	private FavoriteCategoryService favoriteCategoryService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/cups/active/" )
	public SportKindsCupsDTO activeCupsForNavigationTabs( final Principal principal, final HttpServletRequest request ) {

		final User currentUser = getCurrentUser( principal );

		final List<SportKindCupsDTO> list = newArrayList();

		list.addAll( getCupsOfFavoritesCategories( currentUser, AppContext.read( request.getSession() ).getLanguage() ) );

		final Map<SportKind, List<Cup>> sportKindCupsMap = getSportKindCupsMap( currentUser );
		for ( final SportKind sportKind : sportKindCupsMap.keySet() ) {

			final SportKindCupsDTO dto = new SportKindCupsDTO( dtoService.transformSportKind( sportKind ) );

			final List<Cup> cups = sportKindCupsMap.get( sportKind )
					.stream()
					.sorted( cupService.categoryNameOrCupNameComparator() )
					.collect( Collectors.toList() )
					;

			dto.setCups( dtoService.transformCups( cups, currentUser ) );

			list.add( dto );
		}

		return new SportKindsCupsDTO( list );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{sportKindId}/cups/" )
	public List<CategoryCupsDTO> all( final @PathVariable( "sportKindId" ) int sportKindId, final Principal principal ) {

		final User currentUser = getCurrentUser( principal );

		final List<Category> favoriteCategories = favoriteCategoryService.loadUserFavoriteCategories( currentUser );

		final List<Category> categories = categoryService.loadAll( sportKindId )
				.stream()
				.sorted( new Comparator<Category>() {
					@Override
					public int compare( final Category o1, final Category o2 ) {

						if ( favoriteCategories.contains( o1 ) && favoriteCategories.contains( o2 ) ) {
							return o1.getCategoryName().compareToIgnoreCase( o2.getCategoryName() );
						}

						if ( favoriteCategories.contains( o1 ) ) {
							return -1;
						}

						if ( favoriteCategories.contains( o2 ) ) {
							return 1;
						}

						return o1.getCategoryName().compareToIgnoreCase( o2.getCategoryName() );
					}
				} )
				.collect( Collectors.toList() );


		return categories
				.stream()
				.map( new Function<Category, CategoryCupsDTO>() {

					@Override
					public CategoryCupsDTO apply( final Category category ) {

						final CategoryCupsDTO dto = new CategoryCupsDTO();
						dto.setCategory( dtoService.transformCategory( category, currentUser ) );
						dto.setCups( dtoService.transformCups( cupService.loadPublic( category ), currentUser ) );

						return dto;
					}
				} )
				.collect( Collectors.toList() );
	}

	private List<SportKindCupsDTO> getCupsOfFavoritesCategories( final User currentUser, final Language language ) {

		final List<SportKindCupsDTO> result = newArrayList();

		final List<Cup> favoriteCategoriesCups = newArrayList();
		for ( final Category category : favoriteCategoryService.loadUserFavoriteCategories( currentUser ) ) {
			favoriteCategoriesCups.addAll( cupService.loadPublicCurrent( category ) );
		}

		final SportKindDTO fakeSportKindFavorites = new SportKindDTO( 0, translatorService.translate( "Favorite categories cups", language ) );
		final SportKindCupsDTO fakeSportKindDTOFavorites = new SportKindCupsDTO( fakeSportKindFavorites );
		fakeSportKindDTOFavorites.setCups( dtoService.transformCups( favoriteCategoriesCups.stream()
					.sorted( cupService.categoryNameOrCupNameComparator() )
					.collect( Collectors.toList() )
				, currentUser
		) );

		result.add( fakeSportKindDTOFavorites );

		return result;
	}

	private LinkedHashMap<SportKind, List<Cup>> getSportKindCupsMap( final User currentUser ) {

		final LinkedHashMap<SportKind, List<Cup>> result = newLinkedHashMap();

		for ( final SportKind sportKind : sportKindService.loadAll() ) {
			result.put( sportKind, cupService.loadPublicCurrent( sportKind ) );
		}

		return result;
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
