package betmen.web.controllers.rest;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.PortalPageDTO;
import betmen.web.converters.DTOService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/portal-page")
public class PortalPageRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private FavoriteCategoryService favoriteCategoryService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public PortalPageDTO portalPage(final PortalPageDTO dto, final Principal principal) {
        final LocalDate date = dateTimeService.parseDate(dto.getPortalPageDate());
        final User currentUser = userService.findByLogin(principal.getName());

        List<Cup> currentPublicCupsOfFavoriteCategories = cupService.getCurrentPublicCupsOfUserFavoritesCategories(currentUser);

        List<Cup> cupMatchesToShow = Lists.newArrayList(currentPublicCupsOfFavoriteCategories);
        cupMatchesToShow.addAll(cupService.getPublicCupsWhereUserMadeBetsOnDate(currentUser, date));

        final PortalPageDTO result = new PortalPageDTO();
        result.setPortalPageDate(dto.getPortalPageDate());
        result.setCupsToShow(dtoService.transformCups(currentPublicCupsOfFavoriteCategories, currentUser));
        result.setCupsTodayToShow(dtoService.transformCups(cupMatchesToShow, currentUser));
        return result;
    }
}
