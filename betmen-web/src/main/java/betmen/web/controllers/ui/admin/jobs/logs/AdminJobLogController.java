package betmen.web.controllers.ui.admin.jobs.logs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("admin/jobs/logs")
public class AdminJobLogController {

    public static final String MODEL_NAME = "adminJobLogModel";

    private static final String VIEW = "/admin/AdminJobLogs";

    @ModelAttribute(MODEL_NAME)
    public AdminJobLogModel preparePagingModel() {
        return new AdminJobLogModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String main(final @RequestParam("jobTaskId") int jobTaskId, final @ModelAttribute(MODEL_NAME) AdminJobLogModel model) {

        model.setJobTaskId(jobTaskId);

        return VIEW;
    }
}
