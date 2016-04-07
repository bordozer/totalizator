package totalizator.app.controllers.ui.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.matches.MatchService;

import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    public static final String MODEL_NAME = "adminModel";

    private static final String VIEW_MAIN_PAGE = "/admin/AdminPage";
    private static final String VIEW_TRANSLATIONS = "/admin/Translations";

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @ModelAttribute(MODEL_NAME)
    public AdminModel preparePagingModel() {
        return new AdminModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String main(final @ModelAttribute(MODEL_NAME) AdminModel model) {

        Match match = matchService.load(22689);
        match.setCup(cupService.load(22));
        matchService.save(match);

        final List<Cup> currentCups = cupService.loadAllCurrent();
        if (currentCups.size() == 0) {
            return VIEW_MAIN_PAGE;
        }

        final Cup cup = currentCups.get(0);

        model.setCategoryId(cup.getCategory().getId());
        model.setCupId(cup.getId());

        return VIEW_MAIN_PAGE;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/translations/")
    public String translations(final @ModelAttribute(MODEL_NAME) AdminModel model) {
        return VIEW_TRANSLATIONS;
    }
}
