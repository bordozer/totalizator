package betmen.web.controllers.rest;

import betmen.core.entity.SportKind;
import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.SportKindService;
import betmen.core.service.UserService;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
import betmen.dto.dto.SportKindDTO;
import betmen.dto.dto.portal.PortalDefineFavoritesDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/favorites")
public class FavoritesRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FavoriteCategoryService favoriteCategoryService;
    @Autowired
    private SportKindService sportKindService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/categories/")
    public List<CategoryDTO> getAllCurrentUserFavoriteCategories(final Principal principal) {
        return dtoService.transformCategories(favoriteCategoryService.loadUserFavoriteCategories(getCurrentUser(principal)));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/categories/{categoryId}/")
    public void addToCategoryToFavoritesOfCurrentUser(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        favoriteCategoryService.addToFavorites(getCurrentUser(principal), categoryService.loadAndAssertExists(categoryId));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/categories/{categoryId}/")
    public void removeCategoryFromFavoritesOfCurrentUser(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        favoriteCategoryService.removeFromFavorites(getCurrentUser(principal), categoryService.loadAndAssertExists(categoryId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/define/")
    public List<PortalDefineFavoritesDTO> getCategoriesBySport(final Principal principal) {
        User user = getCurrentUser(principal);
        List<SportKind> sportKinds = sportKindService.loadAll();
        return sportKinds.stream()
                .map(sport -> {
                    SportKindDTO sportDto = dtoService.transformSportKind(sport);
                    List<FavoriteCategoryDTO> categoryDTOs = dtoService.transformFavoriteCategories(categoryService.loadAll(sport.getId()), user);
                    return new PortalDefineFavoritesDTO(sportDto, categoryDTOs);
                })
                .collect(Collectors.toList());
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
