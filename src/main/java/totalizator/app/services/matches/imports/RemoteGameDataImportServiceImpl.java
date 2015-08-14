package totalizator.app.services.matches.imports;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.TeamService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.matches.imports.strategies.nba.NBAStatisticsAPIService;
import totalizator.app.services.utils.DateTimeService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class RemoteGameDataImportServiceImpl implements RemoteGameDataImportService {

	@Autowired
	private TeamService teamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private NBAStatisticsAPIService statisticsServerService; // TODO: for new import server implement this

	private final Logger LOGGER = Logger.getLogger( RemoteGameDataImportServiceImpl.class );

	@Override
	public List<String> loadRemoteGameIds( final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {

		final List<String> remoteGamesIds = newArrayList();

		LocalDate date = dateFrom;
		while ( true ) {

			remoteGamesIds.addAll( statisticsServerService.loadRemoteGameIdsOnDate( date ) );

			date = dateTimeService.plusDays( date, 1 );
			if ( date.isAfter( dateTo ) ) {
				break;
			}
		}

		return remoteGamesIds;
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {
		return statisticsServerService.loadRemoteGame( remoteGameId );
	}

	@Override
	public List<RemoteGame> loadRemoteGames( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {

		final List<RemoteGame> result = newArrayList();

		for ( final String remoteGameId : loadRemoteGameIds( dateFrom, dateTo ) ) {

			final RemoteGame remoteGame = loadRemoteGame( remoteGameId );

			if ( remoteGame != null ) {
				result.add( remoteGame );
			}
		}

		return result;
	}

	@Override
	public Match findMatchFor( final Cup cup, final String team1Name, final String team2Name, final LocalDateTime gameDate ) {

		final Team team1 = teamService.findByName( cup.getCategory(), team1Name );

		if ( team1 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", team1Name ) );
			return null;
		}

		final Team team2 = teamService.findByName( cup.getCategory(), team2Name );

		if ( team2 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", team2Name ) );
			return null;
		}

		return matchService.find( cup, team1, team2, gameDate );
	}

	@Override
	public boolean importGame( final Cup cup, final RemoteGame remoteGame ) {

		final String team1Name = remoteGame.getTeam1Name();
		final String team2Name = remoteGame.getTeam2Name();

		final Team team1 = teamService.findByName( cup.getCategory(), team1Name );
		if ( team1 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", team1Name ) );
			return true;
		}

		final Team team2 = teamService.findByName( cup.getCategory(), team2Name );
		if ( team2 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", team2Name ) );
			return true;
		};

		final Match match = findMatchFor( cup, remoteGame.getTeam1Name(), remoteGame.getTeam2Name(), remoteGame.getBeginningTime() );
		if ( match != null ) {
			match.setScore1( remoteGame.getScore1() );
			match.setScore2( remoteGame.getScore2() );
			match.setMatchFinished( remoteGame.isFinished() );
			match.setHomeTeamNumber( remoteGame.getHomeTeamNumber() );

			matchService.save( match );

			return true;
		}

		final Match newMatch = new Match();

		newMatch.setCup( cup );
		newMatch.setBeginningTime( remoteGame.getBeginningTime() );

		newMatch.setTeam1( team1 );
		newMatch.setScore1( remoteGame.getScore1() );

		newMatch.setTeam2( team2 );
		newMatch.setScore2( remoteGame.getScore2() );

		newMatch.setMatchFinished( remoteGame.isFinished() );
		newMatch.setHomeTeamNumber( remoteGame.getHomeTeamNumber() );

		matchService.save( newMatch );

		return true;
	}
}
