package totalizator.app.controllers.ui.admin.pointsStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "/admin/points-calculation-strategies/" )
public class PointsCalculationStrategyController {

	public static final String MODEL_NAME = "pointsCalculationStrategyModel";

	private static final String VIEW = "/admin/PointsCalculationStrategy";

	@Autowired
	private UserService userService;

	@ModelAttribute( MODEL_NAME )
	public PointsCalculationStrategyModel preparePagingModel() {
		return new PointsCalculationStrategyModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "//" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) PointsCalculationStrategyModel model ) {
		return VIEW;
	}
}
