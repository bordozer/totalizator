package totalizator.app.controllers.ui.matches.bets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.Match;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;

@Controller
@RequestMapping( "/totalizator/matches" )
public class MatchBetsController {

	public static final String MODEL_NAME = "matchBetsModel";

	private static final String VIEW = "/MatchBets";

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@ModelAttribute( MODEL_NAME )
	public MatchBetsModel preparePagingModel( final Principal principal ) {
		return new MatchBetsModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bets/" )
	public String portalPage( final @PathVariable( "matchId" ) int matchId, final @ModelAttribute( MODEL_NAME ) MatchBetsModel model ) {

		final Match match = matchService.load( matchId );

		model.setMatch( match );
		model.setMatchTime( dateTimeService.formatDate( match.getBeginningTime() ) );

		return VIEW;
	}
}
