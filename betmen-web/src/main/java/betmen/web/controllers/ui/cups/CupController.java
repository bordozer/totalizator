package betmen.web.controllers.ui.cups;

import betmen.core.service.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/cups")
public class CupController {

    public static final String MODEL_NAME = "cupModel";

    private static final String VIEW = "/Cup";

    @Autowired
    private CupService cupService;

    @ModelAttribute(MODEL_NAME)
    public CupModel preparePagingModel() {
        return new CupModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{cupId}/")
    public String portalPage(final @PathVariable("cupId") int cupId, final @ModelAttribute(MODEL_NAME) CupModel model) {

        model.setCup(cupService.load(cupId));

        return VIEW;
    }
}
