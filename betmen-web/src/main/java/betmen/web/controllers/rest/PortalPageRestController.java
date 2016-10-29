package betmen.web.controllers.rest;

import betmen.core.entity.Category;
import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.CupsAndMatchesService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.PortalPageDTO;
import betmen.web.converters.DTOService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/portal-page")
public class PortalPageRestController {

    private static final Logger LOGGER = Logger.getLogger(PortalPageRestController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CupService cupService;

    @Autowired
    private DTOService dtoService;

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private FavoriteCategoryService favoriteCategoryService;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private CupsAndMatchesService cupsAndMatchesService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public PortalPageDTO portalPage(final PortalPageDTO dto, final Principal principal) {
        final LocalDate date = dateTimeService.parseDate(dto.getPortalPageDate());
        final User currentUser = userService.findByLogin(principal.getName());
        final Comparator<Category> categoryComparator = categoryService.categoriesByFavoritesByName(favoriteCategoryService.loadUserFavoriteCategories(currentUser));
        final PortalPageDTO result = new PortalPageDTO();
        result.setPortalPageDate(dto.getPortalPageDate());
        result.setCupsToShow(dtoService.transformCups(cupService.loadPublicCurrent()
                .stream()
                .sorted((o1, o2) -> {
                    final LocalDateTime withoutBetTime1 = matchBetsService.getFirstMatchWithoutBetTime(o1, currentUser);
                    final LocalDateTime withoutBetTime2 = matchBetsService.getFirstMatchWithoutBetTime(o2, currentUser);
                    if (withoutBetTime1 != null && withoutBetTime2 != null) {
                        return withoutBetTime1.compareTo(withoutBetTime2);
                    }
                    if (withoutBetTime1 != null) {
                        return -1;
                    }
                    if (withoutBetTime2 != null) {
                        return 1;
                    }
                    return categoryComparator.compare(o1.getCategory(), o2.getCategory());
                })
                .collect(Collectors.toList()), currentUser));
        result.setCupsTodayToShow(dtoService.transformCups(cupsAndMatchesService.getPublicCupsHaveMatchesOnDate(date)
                .stream()
                .sorted(cupService.categoryNameOrCupNameComparator())
                .collect(Collectors.toList()), currentUser));
        return result;
    }
}
