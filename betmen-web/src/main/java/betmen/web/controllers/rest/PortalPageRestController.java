package betmen.web.controllers.rest;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.SportKind;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.portal.PortalAnotherMatchesCategory;
import betmen.dto.dto.portal.PortalAnotherMatchesCup;
import betmen.dto.dto.portal.PortalAnotherMatchesSport;
import betmen.dto.dto.portal.PortalPageDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private MatchService matchService;
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
//        result.setAnotherMatchesOnDate(getAnotherMatchesOnDate(currentUser, date));
        return result;
    }

    private List<PortalAnotherMatchesSport> getAnotherMatchesOnDate(final User user, final LocalDate date) {
        List<Category> favoriteCategories = favoriteCategoryService.loadUserFavoriteCategories(user);
        List<Match> matches = matchService.loadAllBetween(dateTimeService.getFirstSecondOf(date), dateTimeService.getLastSecondOf(date));
        List<Match> matchesOfNotFavoriteCategories = matches.stream()
                .filter(match -> !favoriteCategories.contains(match.getCup().getCategory()))
                .collect(Collectors.toList());
        Map<Cup, Long> matchesByCup = matchesOfNotFavoriteCategories.stream()
                .collect(Collectors.toMap(Match::getCup, match -> matchesOfNotFavoriteCategories.stream().filter(game -> game.getCup().equals(match.getCup())).count()));
        List<SportKind> sports = matchesByCup.keySet().stream()
                .map(cup -> cup.getCategory().getSportKind())
                .collect(Collectors.toList());
        return sports.stream()
                .map(sport -> new PortalAnotherMatchesSport(dtoService.transformSportKind(sport), getPortalAnotherMatchesCategories(matchesByCup, sport)))
                .collect(Collectors.toList());
    }

    private List<PortalAnotherMatchesCategory> getPortalAnotherMatchesCategories(final Map<Cup, Long> matchesByCup, final SportKind sport) {
        List<Category> categories = matchesByCup.keySet().stream()
                .map(Cup::getCategory)
                .filter(category -> category.getSportKind().equals(sport))
                .collect(Collectors.toList());
        return categories.stream()
                .map(category -> getPortalAnotherMatchesCategory(matchesByCup, category)).collect(Collectors.toList());
    }

    private PortalAnotherMatchesCategory getPortalAnotherMatchesCategory(final Map<Cup, Long> matchesByCup, final Category category) {
        List<Cup> cups = matchesByCup.keySet().stream().filter(cup -> cup.getCategory().equals(category)).collect(Collectors.toList());
        List<PortalAnotherMatchesCup> anotherCups = cups.stream().map(cup -> new PortalAnotherMatchesCup(dtoService.transformCupItem(cup), matchesByCup.get(cup).intValue())).collect(Collectors.toList());
        return new PortalAnotherMatchesCategory(dtoService.transformCategory(category), anotherCups);
    }
}
