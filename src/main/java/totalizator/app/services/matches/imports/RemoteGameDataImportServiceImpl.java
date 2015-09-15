package totalizator.app.services.matches.imports;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.CupTeamService;
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
import java.util.Set;

import static com.google.common.collect.Sets.newTreeSet;

@Service
public class RemoteGameDataImportServiceImpl implements RemoteGameDataImportService {

	@Autowired
	private TeamService teamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private CupTeamService cupTeamService;

	@Autowired
	private NoStatisticsAPIService noStatisticsAPIService;

	@Autowired
	private NBAStatisticsAPIService nbaStatisticsAPIService;

	@Autowired
	private UEFAStatisticsAPIService uefaStatisticsAPIService;

	private final Logger LOGGER = Logger.getLogger( RemoteGameDataImportServiceImpl.class );

	@Override
	public Set<String> loadRemoteGameIds( final LocalDate dateFrom, final LocalDate dateTo, final Cup cup ) throws IOException {

		final Set<String> remoteGamesIds = newTreeSet();

		final StatisticsServerService statisticsAPIService = getStatisticsServerService( cup );

		LocalDate date = dateFrom;
		while ( true ) {

			remoteGamesIds.addAll( statisticsAPIService.loadRemoteGameIds( cup, date ) );

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
	public void importGame( final Cup cup, final RemoteGame remoteGame ) {

		final String remoteTeam1Id = remoteGame.getRemoteTeam1Id();
		final String remoteTeam2Id = remoteGame.getRemoteTeam2Id();

		final Team team1 = getOrCreateTeam( cup, remoteTeam1Id, remoteGame.getRemoteTeam1Name() );
		final Team team2 = getOrCreateTeam( cup, remoteTeam2Id, remoteGame.getRemoteTeam2Name() );

		final Match match = StringUtils.isNoneEmpty( remoteGame.getRemoteGameId() ) ? findByRemoteGameId( remoteGame.getRemoteGameId() )  : findMatchFor( cup, remoteGame.getRemoteTeam1Id(), remoteGame.getRemoteTeam2Id(), remoteGame.getBeginningTime() );
		if ( match != null ) {
			match.setScore1( remoteGame.getScore1() );
			match.setScore2( remoteGame.getScore2() );
			//match.setBeginningTime(  ); // Do not set beginning time - it can be updated
			match.setMatchFinished( remoteGame.isFinished() );
			match.setHomeTeamNumber( remoteGame.getHomeTeamNumber() );

			matchService.save( match );

			return;
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
	}

	private Team getOrCreateTeam( final Cup cup, final String remoteTeamId, String remoteTeamName ) {

		final Team existsTeam = teamService.findByImportId( cup.getCategory(), remoteTeamId );
		if ( existsTeam != null ) {
			return existsTeam;
		}

		final Team team = new Team();
		team.setTeamName( remoteTeamName );
		team.setImportId( remoteTeamId );
		team.setCategory( cup.getCategory() );

		final Team savedTeam = teamService.save( team );

		cupTeamService.saveCupTeam( cup.getId(), savedTeam.getId(), true );

		return savedTeam;
	}

	private StatisticsServerService getStatisticsServerService( final Cup cup ) {

		final GameImportStrategyType strategyType = GameImportStrategyType.getById( cup.getCategory().getRemoteGameImportStrategyTypeId() );

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

	public void setTeamService( final TeamService teamService ) {
		this.teamService = teamService;
	}

	public void setMatchService( final MatchService matchService ) {
		this.matchService = matchService;
	}

	public void setDateTimeService( final DateTimeService dateTimeService ) {
		this.dateTimeService = dateTimeService;
	}

	public void setCupTeamService( final CupTeamService cupTeamService ) {
		this.cupTeamService = cupTeamService;
	}

	public void setNoStatisticsAPIService( final NoStatisticsAPIService noStatisticsAPIService ) {
		this.noStatisticsAPIService = noStatisticsAPIService;
	}

	public void setNbaStatisticsAPIService( final NBAStatisticsAPIService nbaStatisticsAPIService ) {
		this.nbaStatisticsAPIService = nbaStatisticsAPIService;
	}

	public void setUefaStatisticsAPIService( final UEFAStatisticsAPIService uefaStatisticsAPIService ) {
		this.uefaStatisticsAPIService = uefaStatisticsAPIService;
	}
}
