package totalizator.app.controllers.rest.favorites;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.models.User;
import totalizator.app.services.CategoryService;
import totalizator.app.services.FavoriteCategoryService;
import totalizator.app.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping( "/rest/favorites" )
public class FavoritesRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FavoriteCategoryService favoriteCategoryService;

	@RequestMapping( method = RequestMethod.POST, value = "/categories/{categoryId}/" )
	public void addToCategoryToFavoritesOfCurrentUser( final @PathVariable( "categoryId" ) int categoryId, final Principal principal ) {
		favoriteCategoryService.addToFavorites( getCurrentUser( principal ), categoryService.load( categoryId ) );
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/categories/{categoryId}/" )
	public void removeCategoryFromFavoritesOfCurrentUser( final @PathVariable( "categoryId" ) int categoryId, final Principal principal ) {
		favoriteCategoryService.removeFromFavorites( getCurrentUser( principal ), categoryService.load( categoryId ) );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
