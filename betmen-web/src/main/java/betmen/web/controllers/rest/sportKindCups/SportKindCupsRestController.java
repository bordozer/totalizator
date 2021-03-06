package betmen.web.controllers.rest.sportKindCups;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.SportKind;
import betmen.core.entity.User;
import betmen.core.model.AppContext;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.SportKindService;
import betmen.core.service.UserService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.SportKindDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;

@RestController
@RequestMapping("/rest/sport-kinds/")
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

    @RequestMapping(method = RequestMethod.GET, value = "/cups/active/")
    public SportKindsCupsDTO activeCupsForNavigationTabs(final Principal principal, final HttpServletRequest request) {
        final User currentUser = getCurrentUser(principal);
        final List<SportKindCupsDTO> list = newArrayList();
        list.addAll(getCupsOfFavoritesCategories(currentUser, AppContext.read(request.getSession()).getLanguage()));
        final Map<SportKind, List<Cup>> sportKindCupsMap = getSportKindCupsMap();
        for (final SportKind sportKind : sportKindCupsMap.keySet()) {
            final SportKindCupsDTO dto = new SportKindCupsDTO(dtoService.transformSportKind(sportKind));
            final List<Cup> cups = sportKindCupsMap.get(sportKind)
                    .stream()
                    .sorted(cupService.categoryNameOrCupNameComparator())
                    .collect(Collectors.toList());
            dto.setCupAndCategoryFavorite(convert(currentUser, dtoService.transformCups(cups, currentUser)));
            list.add(dto);
        }
        return new SportKindsCupsDTO(list);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{sportKindId}/cups/")
    public List<CategoryCupsDTO> all(@PathVariable("sportKindId") final int sportKindId, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final List<Category> favoriteCategories = favoriteCategoryService.loadUserFavoriteCategories(currentUser);
        final List<Category> categories = categoryService.loadAll(sportKindId)
                .stream()
                .sorted(categoryService.categoriesByFavoritesByName(favoriteCategories))
                .collect(Collectors.toList());
        return categories.stream()
                .map(category -> {
                    final CategoryCupsDTO dto = new CategoryCupsDTO();
                    dto.setCategory(dtoService.transformFavoriteCategory(category, currentUser));
                    dto.setCups(dtoService.transformCups(cupService.loadPublic(category), currentUser));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private List<SportKindCupsDTO> getCupsOfFavoritesCategories(final User currentUser, final Language language) {
        final List<SportKindCupsDTO> result = newArrayList();
        final List<Cup> favoriteCategoriesCups = newArrayList();
        for (final Category category : favoriteCategoryService.loadUserFavoriteCategories(currentUser)) {
            favoriteCategoriesCups.addAll(cupService.loadPublicCurrent(category));
        }
        final SportKindDTO fakeSportKindFavorites = new SportKindDTO(0, translatorService.translate("Favorite categories cups", language));
        final SportKindCupsDTO fakeSportKindDTOFavorites = new SportKindCupsDTO(fakeSportKindFavorites);
        List<CupDTO> cupDTOs = dtoService.transformCups(favoriteCategoriesCups.stream()
                        .sorted(cupService.categoryNameOrCupNameComparator())
                        .collect(Collectors.toList())
                , currentUser
        );
        fakeSportKindDTOFavorites.setCupAndCategoryFavorite(convert(currentUser, cupDTOs));
        result.add(fakeSportKindDTOFavorites);
        return result;
    }

    private List<CupAndCategoryFavoriteDTO> convert(final User currentUser, final List<CupDTO> cupDTOs) {
        return cupDTOs.stream()
                .map(cup -> new CupAndCategoryFavoriteDTO(cup, favoriteCategoryService.isInFavorites(currentUser.getId(), cup.getCategory().getCategoryId())))
                .collect(Collectors.toList());
    }

    private LinkedHashMap<SportKind, List<Cup>> getSportKindCupsMap() {
        final LinkedHashMap<SportKind, List<Cup>> result = newLinkedHashMap();
        for (final SportKind sportKind : sportKindService.loadAll()) {
            result.put(sportKind, cupService.loadPublicCurrent(sportKind));
        }
        return result;
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
