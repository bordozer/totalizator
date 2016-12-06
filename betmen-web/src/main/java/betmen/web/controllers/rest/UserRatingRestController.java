package betmen.web.controllers.rest;

import betmen.core.model.points.UserSummaryPointsHolder;
import betmen.core.service.points.MatchPointsService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.UsersRatingPositionDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/users/rating/")
public class UserRatingRestController {

    @Autowired
    private MatchPointsService matchPointsService;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<UsersRatingPositionDTO> getUsersRatingOnDate(@RequestParam(value = "dateFrom") final String onDateFrom,
                                                             @RequestParam(value = "dateTo") final String onDateTo) {
        final LocalDate dateFrom = dateTimeService.parseDate(onDateFrom);
        final LocalDate dateTo = dateTimeService.parseDate(onDateTo);
        final List<UserSummaryPointsHolder> usersRatingOnDate = matchPointsService.getUsersRating(dateFrom, dateTo);
        return usersRatingOnDate
                .stream()
                .map(pointsHolder -> new UsersRatingPositionDTO(dtoService.transformUser(pointsHolder.getUser()), pointsHolder.getBetPoints(), pointsHolder.getMatchBonus()))
                .collect(Collectors.toList());
    }
}
