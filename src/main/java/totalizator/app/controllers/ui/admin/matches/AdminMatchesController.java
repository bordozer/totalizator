package totalizator.app.controllers.ui.admin.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;

import java.util.List;

@Controller
@RequestMapping( "admin/matches" )
public class AdminMatchesController {

	public static final String MODEL_NAME = "adminMatchesModel";

	private static final String VIEW_MATCHES = "/admin/AdminMatches";

	@Autowired
	private CupService cupService;

	@ModelAttribute( MODEL_NAME )
	public AdminMatchesModel preparePagingModel() {
		return new AdminMatchesModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String matches( final @ModelAttribute( MODEL_NAME ) AdminMatchesModel model ) {

		final List<Cup> currentCups = cupService.loadAllCurrent();
		if ( currentCups.size() == 0 ) {
			return VIEW_MATCHES;
		}

		final Cup cup = currentCups.get( 0 );

		model.setCategoryId( cup.getCategory().getId() );
		model.setCupId( cup.getId() );

		return VIEW_MATCHES;
	}
}
