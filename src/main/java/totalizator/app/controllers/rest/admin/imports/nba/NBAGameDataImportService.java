package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.imports.*;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.MatchService;
import totalizator.app.services.RemoteContentService;
import totalizator.app.services.TeamService;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class NBAGameDataImportService implements RemoteGameDataImportService {

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private ImportedGamesDataStorageService importedGamesDataStorageService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private RemoteGameService remoteGameService;

	private final GamesDataImportMonitor gamesDataImportMonitor = new GamesDataImportMonitor();

	@Override
	public void startImport() {
		setActivity( true );
	}

	@Override
	public void stopImport() {
		setActivity( false );
	}

	@Override
	public boolean isImportingNow() {
		return gamesDataImportMonitor.isImportActive();
	}

	@Override
	public boolean importGame( final Cup cup, final String gameId ) throws IOException {

		final String gameJSON = getRemoteGameJSON( gameId );
		if ( StringUtils.isEmpty( gameJSON ) ) {
			return false;
		}

		final RemoteGame remoteGame = remoteGameService.parseGame( gameJSON );

		final String team1Name = remoteGame.getTeam1Name();
		final String team2Name = remoteGame.getTeam2Name();

		final LocalDateTime gameDate = remoteGame.getGameDate();

		final Team team1 = teamService.findByName( team1Name );

		if ( team1 == null ) {
			throw new IllegalArgumentException( String.format( "Team name '%s' not found", team1Name ) );
		}

		final Team team2 = teamService.findByName( team2Name );

		if ( team2 == null ) {
			throw new IllegalArgumentException( String.format( "Team name '%s' not found", team2Name ) );
		}

		final Match match = matchService.find( team1, team2, gameDate );
		if ( match != null ) {
			match.setScore1( remoteGame.getScore1() );
			match.setScore2( remoteGame.getScore2() );
			match.setMatchFinished( remoteGame.isFinished() );

			matchService.save( match );

			return true;
		}

		final Match newMatch = new Match();

		newMatch.setCup( cup );
		newMatch.setBeginningTime( gameDate );

		newMatch.setTeam1( teamService.findByName( team1Name ) );
		newMatch.setScore1( remoteGame.getScore1() );

		newMatch.setTeam2( teamService.findByName( team2Name ) );
		newMatch.setScore2( remoteGame.getScore2() );

		newMatch.setMatchFinished( remoteGame.isFinished() );

		matchService.save( newMatch );

		return true;
	}

	private String getRemoteGameJSON( final String remoteGameId ) throws IOException {

		final String gameData = importedGamesDataStorageService.getGameData( remoteGameId );
		if ( StringUtils.isNotEmpty( gameData ) ) {
			return gameData;
		}

		final String gameJSON = remoteContentService.getRemoteContent( String.format( "http://stats.nba.com/stats/boxscore?GameID=%s&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0", remoteGameId ) );

		if ( StringUtils.isNotEmpty( gameJSON ) ) {
			importedGamesDataStorageService.store( remoteGameId, gameJSON );
		}

		return gameJSON;
	}

	private void setActivity( final boolean importActive ) {
		synchronized ( gamesDataImportMonitor ) {
			gamesDataImportMonitor.setImportActive( importActive );
		}
	}
}
