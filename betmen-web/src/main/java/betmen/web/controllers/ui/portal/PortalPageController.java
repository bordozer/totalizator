package betmen.web.controllers.ui.portal;

import betmen.core.service.utils.DateTimeService;
import betmen.web.utils.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(Parameters.PORTAL_PAGE_URL)
public class PortalPageController {

    public static final String MODEL_NAME = "portalPageModel";

    private static final String VIEW = "/PortalPage";

    @Autowired
    private DateTimeService dateTimeService;

    @ModelAttribute(MODEL_NAME)
    public PortalPageModel preparePagingModel() {
        return new PortalPageModel(dateTimeService.formatDate(dateTimeService.getToday()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public String portalPage(@ModelAttribute(MODEL_NAME) final PortalPageModel model) {
        return VIEW;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{day}/{month}/{year}/")
    public String portalPageOnDate(@ModelAttribute(MODEL_NAME) final PortalPageModel model, @PathVariable final int day, @PathVariable final int month, @PathVariable final int year) {
        model.setOnDate(String.format("%02d/%02d/%d", day, month, year));
        return VIEW;
    }
}
