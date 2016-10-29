package betmen.web.controllers.ui.user.card;

import betmen.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/betmen/users/{userId}")
public class UserCardController {

    public static final String MODEL_NAME = "userCardModel";

    private static final String VIEW = "/UserCard";

    @Autowired
    private UserService userService;

    @ModelAttribute(MODEL_NAME)
    public UserCardModel preparePagingModel(final @PathVariable("userId") int userId) {
        return new UserCardModel(userService.load(userId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage(final @ModelAttribute(MODEL_NAME) UserCardModel model, final @RequestParam(value = "cupId", required = false) Integer cupId) {

        model.setFilterByCupId(cupId != null ? cupId : 0);

        return VIEW;
    }
}
