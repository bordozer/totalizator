package betmen.web.controllers.ui.cups.winners.bets;

import betmen.core.service.CupService;
import betmen.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/cups/{cupId}/winners/bets/")
public class CupWinnersBetsController {

    public static final String MODEL_NAME = "cupWinnersBetsModel";

    private static final String VIEW = "/CupWinnersBets";

    @Autowired
    private UserService userService;

    @Autowired
    private CupService cupService;

    @ModelAttribute(MODEL_NAME)
    public CupWinnersBetsModel preparePagingModel() {
        return new CupWinnersBetsModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage(final @PathVariable("cupId") int cupId, final @ModelAttribute(MODEL_NAME) CupWinnersBetsModel model) {

        model.setCup(cupService.load(cupId));

        return VIEW;
    }
}
