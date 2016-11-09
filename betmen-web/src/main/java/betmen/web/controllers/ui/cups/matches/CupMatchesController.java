package betmen.web.controllers.ui.cups.matches;

import betmen.core.service.CupService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;

@Controller
@RequestMapping("/betmen/cups/{cupId}/matches")
public class CupMatchesController {

    public static final String MODEL_NAME = "cupMatchesModel";

    private static final String VIEW = "/CupMatches";

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private CupService cupService;

    @ModelAttribute(MODEL_NAME)
    public CupMatchesModel preparePagingModel() {
        return new CupMatchesModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String cupMatches(@PathVariable("cupId") final int cupId, @ModelAttribute(MODEL_NAME) final CupMatchesModel model) {
        model.setCup(cupService.loadAndAssertExists(cupId));
        LocalDate today = dateTimeService.getToday();
        model.setOnDate(String.format("%02d/%02d/%d", today.getDayOfMonth(), today.getMonthValue(), today.getYear()));
        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{day}/{month}/{year}/")
    public String cupMatchesOnDate(@PathVariable("cupId") final int cupId, @ModelAttribute(MODEL_NAME) final CupMatchesModel model, @PathVariable final int day, @PathVariable final int month, @PathVariable final int year) {
        model.setCup(cupService.loadAndAssertExists(cupId));
        model.setOnDate(String.format("%02d/%02d/%d", day, month, year));
        return VIEW;
    }
}
