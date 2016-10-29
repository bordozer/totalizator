package betmen.web.controllers.rest.favorites;

import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/rest/favorites")
public class FavoritesRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FavoriteCategoryService favoriteCategoryService;

    @RequestMapping(method = RequestMethod.POST, value = "/categories/{categoryId}/")
    public void addToCategoryToFavoritesOfCurrentUser(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        favoriteCategoryService.addToFavorites(getCurrentUser(principal), categoryService.load(categoryId));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/categories/{categoryId}/")
    public void removeCategoryFromFavoritesOfCurrentUser(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        favoriteCategoryService.removeFromFavorites(getCurrentUser(principal), categoryService.load(categoryId));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
