package totalizator.app.services.matches.imports;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.TeamService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.matches.imports.strategies.NoStatisticsAPIService;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;
import totalizator.app.services.matches.imports.strategies.nba.NBAStatisticsAPIService;
import totalizator.app.services.matches.imports.strategies.uefa.UEFAStatisticsAPIService;
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
	private ImportUtilsService importUtilsService;

	@Autowired
	private NoStatisticsAPIService noStatisticsAPIService;

	@Autowired
	private NBAStatisticsAPIService nbaStatisticsAPIService;

	@Autowired
	private UEFAStatisticsAPIService uefaStatisticsAPIService;

	private final Logger LOGGER = Logger.getLogger( RemoteGameDataImportServiceImpl.class );

	@Override
	public List<String> loadRemoteGameIds( final LocalDate dateFrom, final LocalDate dateTo, final Cup cup ) throws IOException {

		final List<String> remoteGamesIds = newArrayList();

		final StatisticsServerService statisticsAPIService = getStatisticsServerService( cup );

		LocalDate date = dateFrom;
		while ( true ) {

			remoteGamesIds.addAll( statisticsAPIService.loadRemoteGameIdsOnDate( cup, date ) );

			date = dateTimeService.plusDays( date, 1 );
			if ( date.isAfter( dateTo ) ) {
				break;
			}
		}

		return remoteGamesIds;
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId, final Cup cup ) throws IOException {
		return getStatisticsServerService( cup ).loadRemoteGame( remoteGameId );
	}

	@Override
	public List<RemoteGame> loadRemoteGames( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {

		final List<RemoteGame> result = newArrayList();

		for ( final String remoteGameId : loadRemoteGameIds( dateFrom, dateTo, cup ) ) {

			final RemoteGame remoteGame = loadRemoteGame( remoteGameId, cup );

			if ( remoteGame != null ) {
				result.add( remoteGame );
			}
		}

		return result;
	}

	@Override
	public Match findMatchFor( final Cup cup, final String remoteTeam1Id, final String remoteTeam2Id, final LocalDateTime gameDate ) {

		final Team team1 = teamService.findByImportId( cup.getCategory(), remoteTeam1Id );

		if ( team1 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", remoteTeam1Id ) );
			return null;
		}

		final Team team2 = teamService.findByImportId( cup.getCategory(), remoteTeam2Id );

		if ( team2 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", remoteTeam2Id ) );
			return null;
		}

		return matchService.find( cup, team1, team2, gameDate );
	}

	@Override
	public Match findByRemoteGameId( final String remoteGameId ) {
		return matchService.findByImportId( remoteGameId );
	}

	@Override
	public boolean importGame( final Cup cup, final RemoteGame remoteGame ) {

		final String remoteTeam1Id = remoteGame.getRemoteTeam1Id();
		final String remoteTeam2Id = remoteGame.getRemoteTeam2Id();

		final Team team1 = teamService.findByImportId( cup.getCategory(), remoteTeam1Id );
		if ( team1 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", remoteTeam1Id ) );
			return true;
		}

		final Team team2 = teamService.findByImportId( cup.getCategory(), remoteTeam2Id );
		if ( team2 == null ) {
			LOGGER.warn( String.format( "Team '%s' not found. Game import skipped", remoteTeam2Id ) );
			return true;
		};

		final Match match = findMatchFor( cup, remoteGame.getRemoteTeam1Id(), remoteGame.getRemoteTeam2Id(), remoteGame.getBeginningTime() );
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

		newMatch.setRemoteGameId( remoteGame.getRemoteGameId() );

		matchService.save( newMatch );

		return true;
	}

	private StatisticsServerService getStatisticsServerService( final Cup cup ) {

		final GameImportStrategyType strategyType = importUtilsService.getGameImportStrategyType( cup );

		switch ( strategyType ) {
			case NO_IMPORT:
				return noStatisticsAPIService;
			case NBA:
				return nbaStatisticsAPIService;
			case UEFA:
				return uefaStatisticsAPIService;
			default:
				throw new IllegalArgumentException( String.format( "Unsupported GameImportStrategyType: %s", strategyType ) );
		}
	}
}
