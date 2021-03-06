package betmen.web.controllers.rest;

import betmen.core.entity.*;
import betmen.core.service.CupService;
import betmen.core.service.FavoriteCategoryService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.portal.*;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private MatchBetsService matchBetsService;
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
        result.setAnotherMatchesOnDate(getAnotherMatchesOnDate(currentUser, date));
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/favorites/statistics/")
    public FavoriteCategoriesBetStatisticsDTO portalPageFavoriteCategoriesBetStatistics(@RequestParam(value = "onDate") final String onDate,
                                                                                        final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final LocalDate date = dateTimeService.parseDate(onDate);
        final FavoriteCategoriesBetStatisticsDTO dto = new FavoriteCategoriesBetStatisticsDTO();
        dto.setOnDate(date);
        dto.setCategoryBetStatistics(favoriteCategoryService.loadUserFavoriteCategories(currentUser).stream()
                .map(category -> {
                    final CategoryDTO categoryDTO = dtoService.transformCategory(category);
                    final int matchesCount = matchService.getMatchCount(category.getId(), date);
                    if (matchesCount == 0) {
                        return null;
                    }
                    int betsCount = matchBetsService.userBetsCount(currentUser, category, date);
                    return new FavoriteCategoryBetStatisticsDTO(categoryDTO, matchesCount, betsCount);
                })
                .filter(res -> res != null)
                .collect(Collectors.toList())
        );
        return dto;
    }

    private List<PortalAnotherMatchesSportDTO> getAnotherMatchesOnDate(final User user, final LocalDate date) {
        List<Category> favoriteCategories = favoriteCategoryService.loadUserFavoriteCategories(user);
        List<Match> matches = matchService.loadAllBetween(dateTimeService.getFirstSecondOf(date), dateTimeService.getLastSecondOf(date));
        List<Match> matchesOfNotFavoriteCategories = matches.stream()
                .filter(match -> !favoriteCategories.contains(match.getCup().getCategory()))
                .collect(Collectors.toList());
        List<Cup> cups = matchesOfNotFavoriteCategories.stream()
                .map(Match::getCup)
                .distinct()
                .collect(Collectors.toList());
        Map<Cup, Long> matchesByCup = cups.stream()
                .collect(Collectors.toMap(cup -> cup, cup -> matchesOfNotFavoriteCategories.stream().filter(game -> game.getCup().equals(cup)).count()));
        List<SportKind> sports = matchesByCup.keySet().stream()
                .map(cup -> cup.getCategory().getSportKind())
                .distinct()
                .collect(Collectors.toList());
        return sports.stream()
                .map(sport -> new PortalAnotherMatchesSportDTO(dtoService.transformSportKind(sport), getPortalAnotherMatchesCategories(matchesByCup, sport)))
                .collect(Collectors.toList());
    }

    private List<PortalAnotherMatchesCategoryDTO> getPortalAnotherMatchesCategories(final Map<Cup, Long> matchesByCup, final SportKind sport) {
        List<Category> categories = matchesByCup.keySet().stream()
                .map(Cup::getCategory)
                .filter(category -> category.getSportKind().equals(sport))
                .distinct()
                .collect(Collectors.toList());
        return categories.stream()
                .map(category -> getPortalAnotherMatchesCategory(matchesByCup, category)).collect(Collectors.toList());
    }

    private PortalAnotherMatchesCategoryDTO getPortalAnotherMatchesCategory(final Map<Cup, Long> matchesByCup, final Category category) {
        List<Cup> cups = matchesByCup.keySet().stream().filter(cup -> cup.getCategory().equals(category)).collect(Collectors.toList());
        List<PortalAnotherMatchesCupDTO> anotherCups = cups.stream().map(cup -> new PortalAnotherMatchesCupDTO(dtoService.transformCupItem(cup), matchesByCup.get(cup).intValue())).collect(Collectors.toList());
        return new PortalAnotherMatchesCategoryDTO(dtoService.transformCategory(category), anotherCups);
    }
}
