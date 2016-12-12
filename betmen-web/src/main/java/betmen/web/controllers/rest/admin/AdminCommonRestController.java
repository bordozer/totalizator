package betmen.web.controllers.rest.admin;

import betmen.core.service.activiries.ActivityStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/rest/common")
public class AdminCommonRestController {

    @Autowired
    private ActivityStreamService activityStreamService;

    @RequestMapping(method = RequestMethod.GET, value = "/activities/cleanup/")
    public boolean getItem() {
        activityStreamService.deleteAll();
        return true;
    }
}
