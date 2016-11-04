package betmen.web.controllers.rest.categories;

import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
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
@RequestMapping("/rest/categories")
public class CategoriesRestController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<CategoryDTO> entries(final Principal principal) {
        return dtoService.transformCategories(categoryService.loadAll());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sportKind/{sportKindId}/")
    public List<CategoryDTO> sportKindsCategories(@PathVariable("sportKindId") final int sportKindId, final Principal principal) {
        return dtoService.transformCategories(categoryService.loadAll().stream()
                        .filter(category -> category.getSportKind().getId() == sportKindId)
                        .collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{categoryId}/")
    public FavoriteCategoryDTO entry(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        return dtoService.transformFavoriteCategory(categoryService.load(categoryId), geCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{categoryId}/cups/public/")
    public List<CupDTO> categoryCaps(@PathVariable("categoryId") final int categoryId, final Principal principal) {
        return dtoService.transformCups(cupService.loadPublic(categoryService.load(categoryId)), geCurrentUser(principal));
    }

    private User geCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
