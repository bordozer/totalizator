package betmen.web.controllers.rest.user.card;

import betmen.core.entity.AbstractEntity;
import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.CupDTO;
import betmen.web.converters.DTOService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/users/{userId}")
public class UserCardRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private MatchBetsService matchBetsService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/card/")
    public UserCardDTO userCard(@PathVariable("userId") final int userId, @RequestParam(value = "cupId", required = false) final Integer cupId,
                                final UserCardParametersDTO parameters, final Principal principal) {

        final User currentUser = userService.findByLogin(principal.getName());
        final LocalDate date = dateTimeService.parseDate(parameters.getOnDate());
        final List<Cup> cupsByParameters = getCupsToShow(currentUser, cupId, date);
        final List<CupDTO> cupDTOs = dtoService.transformCups(cupsByParameters
                .stream()
                .sorted(cupService.categoryNameOrCupNameComparator())
                .collect(Collectors.toList()), currentUser);
        return new UserCardDTO(cupDTOs);
    }

    private List<Cup> getCupsToShow(final User user, final Integer cupId, final LocalDate date) {
        List<Integer> cupIdsWhereUserHasBets = getCupsWhereUserHasBets(user.getId(), date).stream().map(AbstractEntity::getId).collect(Collectors.toList());
        if (cupId != null && cupId > 0) {
            return Lists.newArrayList(cupService.load(cupId));
        }
        return cupService.getUserCupsOnDate(user, date)
                .stream()
                .filter(cup -> cupIdsWhereUserHasBets.contains(cup.getId()))
                .collect(Collectors.toList());
    }

    private List<Cup> getCupsWhereUserHasBets(@PathVariable("userId") final int userId, final LocalDate date) {
        final User userWhoBetsIsShown = userService.load(userId);
        return matchBetsService.loadAll(userWhoBetsIsShown, date)
                .stream()
                .map(matchBet -> matchBet.getMatch().getCup())
                .distinct()
                .collect(Collectors.toList());
    }
}
