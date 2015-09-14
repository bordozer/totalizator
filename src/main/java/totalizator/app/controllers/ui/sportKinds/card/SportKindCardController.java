package totalizator.app.controllers.ui.sportKinds.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.SportKindService;

@Controller
@RequestMapping( "/totalizator/sports/{sportKindId}" )
public class SportKindCardController {

	public static final String MODEL_NAME = "sportKindCardModel";

	private static final String VIEW = "/SportKindCard";

	@Autowired
	private SportKindService sportKindService;

	@ModelAttribute( MODEL_NAME )
	public SportKindCardModel preparePagingModel( final @PathVariable( "sportKindId" ) int sportKindId ) {
		return new SportKindCardModel( sportKindService.load( sportKindId ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public String portalPage( final @ModelAttribute( MODEL_NAME ) SportKindCardModel model ) {
		return VIEW;
	}
}
