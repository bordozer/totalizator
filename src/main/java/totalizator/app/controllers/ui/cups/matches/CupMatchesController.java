package totalizator.app.controllers.ui.cups.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.services.CupService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

@Controller
@RequestMapping( "/totalizator/cups" )
public class CupMatchesController {

	public static final String MODEL_NAME = "cupMatchesModel";

	private static final String VIEW = "/CupMatches";

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupService cupService;

	@ModelAttribute( MODEL_NAME )
	public CupMatchesModel preparePagingModel() {
		return new CupMatchesModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/matches/" )
	public String cupMatches( final @PathVariable( "cupId" ) int cupId
			, final @ModelAttribute( MODEL_NAME ) CupMatchesModel model ) {

		model.setCup( cupService.load( cupId ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/matches/teams/{teamId}/" )
	public String cupMatchesForTeam( final @PathVariable( "cupId" ) int cupId
			, final @PathVariable( "teamId" ) int teamId
			, final @ModelAttribute( MODEL_NAME ) CupMatchesModel model ) {

		model.setCup( cupService.load( cupId ) );
		model.setTeam1( teamService.load( teamId ) );

		return VIEW;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/{cupId}/matches/teams/{team1Id}/vs/{team2Id}/" )
	public String cupMatchesForTeam1( final @PathVariable( "cupId" ) int cupId
			, final @PathVariable( "team1Id" ) int team1Id
			, final @PathVariable( "team2Id" ) int team2Id
			, final @ModelAttribute( MODEL_NAME ) CupMatchesModel model ) {

		model.setCup( cupService.load( cupId ) );
		model.setTeam1( teamService.load( team1Id ) );
		model.setTeam2( teamService.load( team2Id ) );

		return VIEW;
	}
}
