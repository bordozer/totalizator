package betmen.web.controllers.rest;

import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.UserCardDTO;
import betmen.dto.dto.UserCardParametersDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
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
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/card/")
    public UserCardDTO userCard(@PathVariable("userId") final int userId,
                                @RequestParam(value = "cupId", required = false) final Integer cupId,
                                @Validated final UserCardParametersDTO parameters) {
        final User user = userService.loadAndAssertExists(userId);
        final LocalDate date = dateTimeService.parseDate(parameters.getOnDate());
        final List<Cup> cupsToShow = getCupsToShow(user, cupId, date);
        return new UserCardDTO(dtoService.transformCupItems(cupsToShow));
    }

    private List<Cup> getCupsToShow(final User user, final Integer cupId, final LocalDate date) {
        if (cupId != null && cupId > 0) {
            return Collections.singletonList(cupService.loadAndAssertExists(cupId));
        }
        return cupService.getUserCupsOnDate(user, date)
                .stream()
                .collect(Collectors.toList());
    }
}
