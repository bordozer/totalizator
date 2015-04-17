package totalizator.app.controllers.ui.teams.standoffs;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping( "/totalizator/teams/" )
public class TeamsStandoffsController {

	public static final String MODEL_NAME = "teamsStandoffsModel";

	private static final String VIEW = "/TeamsStandoffs";

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DTOService dtoService;

	@ModelAttribute( MODEL_NAME )
	public TeamsStandoffsModel preparePagingModel( final Principal principal ) {
		return new TeamsStandoffsModel( userService.findByLogin( principal.getName() ) );
	}

	@RequestMapping( method = RequestMethod.GET, value = "/standoff/{team1Id}/vs/{team2Id}/" )
	public String portalPage( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id, final @ModelAttribute( MODEL_NAME ) TeamsStandoffsModel model ) {

		final User currentUser = model.getCurrentUser();

		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		final List<Match> matches = matchService.find( team1, team2 );

		model.setMatchesAndBetsJSON( new Gson().toJson( dtoService.getMatchBetForMatches( matches, currentUser ) ) );

		return VIEW;
	}
}
