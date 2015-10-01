package totalizator.app.controllers.rest.admin.jobs.results;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;
import totalizator.app.services.jobs.JobLogService;
import totalizator.app.services.jobs.jobDTO.JobDTOService;
import totalizator.app.services.jobs.results.GamesImportJobLogResultJSON;
import totalizator.app.services.jobs.results.ImportedRemoteGame;
import totalizator.app.services.matches.MatchService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/admin/rest/jobs/custom-results" )
public class AdminJobTaskCustomResultsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private JobLogService jobLogService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private JobDTOService jobDTOService;

	@RequestMapping( method = RequestMethod.GET, value = "/games-import/logs/{jobTaskLogId}/imported-games/" )
	public List<RemoteGameMatchPair> matchesForArray( final @PathVariable( "jobTaskLogId" ) int jobTaskLogId, final Principal principal ) {

		final User currentUser = getCurrentUser( principal );

		final GamesImportJobLogResultJSON resultJSON = jobDTOService.transformFromJobGamesImportSpecificResultJSON( jobLogService.load( jobTaskLogId ) );

		return resultJSON.getImportedRemoteGames()
				.stream()
				.map( new Function<ImportedRemoteGame, RemoteGameMatchPair>() {

					@Override
					public RemoteGameMatchPair apply( final ImportedRemoteGame importedRemoteGame ) {

						final RemoteGameMatchPair matchPair = new RemoteGameMatchPair( importedRemoteGame.getRemoteGame() );

						matchPair.setIsNewGameCreated( importedRemoteGame.isNewGameCreated() );
						matchPair.setMatchId( importedRemoteGame.getMatchId() );

						final Match match = matchService.load( importedRemoteGame.getMatchId() );

						if ( match == null ) {
							return matchPair;
						}

						final MatchDTO matchDTO = dtoService.transformMatch( match, currentUser );
						matchPair.setMatch( matchDTO );

						return matchPair;
					}
				} )
				.collect( Collectors.toList() );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}
