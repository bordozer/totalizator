package betmen.web.controllers.ui.favorites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/favorites")
public class FavoritesController {
    public static final String MODEL_NAME = "favoritesModel";
    private static final String VIEW = "/Favorites";

    @ModelAttribute(MODEL_NAME)

    public FavoritesModel preparePagingModel() {
        return new FavoritesModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage(@ModelAttribute(MODEL_NAME) final FavoritesModel model) {
        return VIEW;
    }
}
