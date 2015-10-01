package totalizator.app.controllers.ui.admin.jobs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "admin/jobs" )
public class AdminJobsController {

	public static final String MODEL_NAME = "adminJobsModel";

	private static final String VIEW = "/admin/AdminJobs";

	@ModelAttribute( MODEL_NAME )
	public AdminJobsModel preparePagingModel() {
		return new AdminJobsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String main( final @ModelAttribute( MODEL_NAME ) AdminJobsModel model ) {
		return VIEW;
	}
}
