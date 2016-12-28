package betmen.web.controllers.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/guitar")
public class GuitarController {

    private static final String VIEW = "/Guitar";

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage() {
        return VIEW;
    }
}
