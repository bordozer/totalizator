package totalizator.app.controllers.ui.teams.standoffs;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

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

		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		model.setTeam1( team1 );
		model.setTeam1JSON( new Gson().toJson( dtoService.transformTeam( team1 ) ) );
		model.setTeam2( team2 );
		model.setTeam2JSON( new Gson().toJson( dtoService.transformTeam( team2 ) ) );

		final List<Match> matches = matchService.find( team1, team2 );
		final Set<Cup> cupsSet = newHashSet();
		for ( final Match match : matches ) {
			if ( matchService.isMatchFinished( match ) ) {
				cupsSet.add( match.getCup() );
			}
		}
		final List<Cup> cups = newArrayList( cupsSet );
		final List<CupDTO> cupDTOs = dtoService.transformCups( cups, model.getCurrentUser() );
		model.setCups( new Gson().toJson( cupDTOs ) );

		final int score1 = 0;
		final int score2 = 1;
		model.setScore1( score1 );
		model.setScore2( score2 );

		return VIEW;
	}
}
