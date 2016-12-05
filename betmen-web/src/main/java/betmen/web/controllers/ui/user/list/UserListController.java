package betmen.web.controllers.ui.user.list;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/users")
public class UserListController {

    public static final String MODEL_NAME = "userListModel";

    private static final String VIEW = "/UserList";

    @ModelAttribute(MODEL_NAME)
    public UserListModel preparePagingModel() {
        return new UserListModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage(final @ModelAttribute(MODEL_NAME) UserListModel model) {
        return VIEW;
    }
}
