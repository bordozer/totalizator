package betmen.web.controllers.ui.portal;

import betmen.web.utils.Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects to default page
 */
@Controller
@RequestMapping("")
public class DefaultPageController {

    @RequestMapping(method = RequestMethod.GET)
    public void redirectToPortalPage(HttpServletResponse response) throws IOException {
        response.sendRedirect(Parameters.PORTAL_PAGE_URL);
    }
}
