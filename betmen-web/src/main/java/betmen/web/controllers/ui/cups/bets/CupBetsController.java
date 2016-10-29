package betmen.web.controllers.ui.cups.bets;

import betmen.core.service.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/cups")
public class CupBetsController {

    public static final String MODEL_NAME = "cupBetsModel";

    private static final String VIEW = "/CupBets";

    @Autowired
    private CupService cupService;

    @ModelAttribute(MODEL_NAME)
    public CupBetsModel preparePagingModel() {
        return new CupBetsModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cupId}/bets/")
    public String portalPage(final @PathVariable("cupId") int cupId, final @ModelAttribute(MODEL_NAME) CupBetsModel model) {

        model.setCup(cupService.load(cupId));

        return VIEW;
    }
}
