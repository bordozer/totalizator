package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.PortalPageDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

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

        final PortalPageDTO result = new PortalPageDTO();
        result.setPortalPageDate(dto.getPortalPageDate());
        result.setCupsToShow(dtoService.transformCups(cupService.getCurrentPublicCupsOfUserFavoritesCategories(currentUser), currentUser));
        result.setCupsTodayToShow(dtoService.transformCups(cupService.getUserCupsOnDate(currentUser, date), currentUser));
        return result;
    }
}
