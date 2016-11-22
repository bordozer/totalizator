package betmen.web.controllers.ui.user.card;

import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/betmen/users/{userId}")
public class UserCardController {

    public static final String MODEL_NAME = "userCardModel";

    private static final String VIEW = "/UserCard";

    @Autowired
    private UserService userService;

    @Autowired
    private DateTimeService dateTimeService;

    @ModelAttribute(MODEL_NAME)
    public UserCardModel preparePagingModel(@PathVariable("userId") final int userId) {
        return new UserCardModel(userService.load(userId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String userPage(@ModelAttribute(MODEL_NAME) final UserCardModel model, @RequestParam(value = "cupId", required = false) final Integer cupId) {
        model.setFilterByCupId(cupId != null ? cupId : 0);
        LocalDate today = dateTimeService.getToday();
        model.setOnDate(String.format("%02d/%02d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()));
        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{day}/{month}/{year}/")
    public String portalPageOnDate(@ModelAttribute(MODEL_NAME) final UserCardModel model, @PathVariable final int day, @PathVariable final int month, @PathVariable final int year) {
        model.setOnDate(String.format("%02d/%02d/%d", day, month, year));
        return VIEW;
    }
}
