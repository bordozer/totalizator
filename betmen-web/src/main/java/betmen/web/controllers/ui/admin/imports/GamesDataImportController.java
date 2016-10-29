package betmen.web.controllers.ui.admin.imports;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("admin/remote-games-import")
public class GamesDataImportController {

    public static final String MODEL_NAME = "gamesDataImportModel";

    private static final String VIEW = "/admin/GamesDataImport";

    @ModelAttribute(MODEL_NAME)
    public GamesDataImportModel preparePagingModel() {
        return new GamesDataImportModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String main(final @ModelAttribute(MODEL_NAME) GamesDataImportModel model) {
        return VIEW;
    }
}
