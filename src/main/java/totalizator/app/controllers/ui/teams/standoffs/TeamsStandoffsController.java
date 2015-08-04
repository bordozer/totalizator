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
import totalizator.app.services.*;

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
	private CupService cupService;

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
		return new TeamsStandoffsModel();
	}

	@RequestMapping( method = RequestMethod.GET, value = "/standoff/{team1Id}/vs/{team2Id}/" )
	public String portalPage( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id
			, final @ModelAttribute( MODEL_NAME ) TeamsStandoffsModel model, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		model.setTeam1( team1 );
		model.setTeam1JSON( new Gson().toJson( dtoService.transformTeam( team1 ) ) );
		model.setTeam2( team2 );
		model.setTeam2JSON( new Gson().toJson( dtoService.transformTeam( team2 ) ) );

		final List<Match> matches = matchService.loadAll( team1, team2 );
		final Set<Cup> cupsSet = newHashSet();
		for ( final Match match : matches ) {
			if ( matchService.isMatchFinished( match ) ) {
				cupsSet.add( match.getCup() );
			}
		}
		final List<Cup> cups = newArrayList( cupsSet );
		cupService.sort( cups );
		final List<CupDTO> cupDTOs = dtoService.transformCups( cups, currentUser );
		model.setCups( new Gson().toJson( cupDTOs ) );

		final int score1 = getScore( matches, team1 );
		final int score2 = getScore( matches, team2 );
		model.setScore1( score1 );
		model.setScore2( score2 );

		return VIEW;
	}

	private int getScore( final List<Match> matches, final Team team ) {
		int result = 0;

		for ( final Match match : matches ) {
			result += match.getScore1() > match.getScore2() && match.getTeam1().equals( team ) ? 1 : 0;
			result += match.getScore1() < match.getScore2() && match.getTeam2().equals( team ) ? 1 : 0;
		}

		return result;
	}
}
