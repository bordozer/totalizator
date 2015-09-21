package totalizator.app.services.matches.imports.strategies.nhl;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.services.TeamService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;
import totalizator.app.services.remote.RemoteContentService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class NHLStatisticsAPIService implements StatisticsServerService {

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private NHLStatisticsServerURLService nhlStatisticsServerURLService;

	@Override
	public Set<RemoteGame> loadGamesFromJSON( final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) throws IOException {

		final Set<RemoteGame> result = new TreeSet<RemoteGame>();

		for ( final Team team : teamService.loadAll( cup.getCategory() ) ) {
			result.addAll( loadRemoteGame( team, dateFrom ) );
		}

		return result;
	}

	@Override
	public void loadGameFromJSON( final Cup cup, final RemoteGame remoteGame ) throws IOException {
	}

	private Set<RemoteGame> loadRemoteGame( final Team team, final LocalDate dateFrom ) throws IOException {

		final String teamImportId = team.getImportId();

		final String url = nhlStatisticsServerURLService.getURL( team, dateFrom );
		final String remoteGamesJSON = remoteContentService.getRemoteContent( url );

		final Set<RemoteGame> result = new TreeSet<RemoteGame>();

		final Gson gson = new Gson();

		final LinkedTreeMap json = gson.fromJson( remoteGamesJSON, LinkedTreeMap.class );

		final List<LinkedTreeMap> games = ( List<LinkedTreeMap> ) json.get( "games" );
		for ( final LinkedTreeMap game : games ) {

			final RemoteGame remoteGame = new RemoteGame( new BigDecimal( ( Double ) game.get( "gameId" ) ).toString() );

			remoteGame.setRemoteTeam1Id( teamImportId );
			remoteGame.setRemoteTeam1Name( teamImportId );

			final String remoteTeam2Id = ( String ) game.get( "abb" );
			remoteGame.setRemoteTeam2Id( remoteTeam2Id );
			remoteGame.setRemoteTeam2Name( remoteTeam2Id );

			final String score = ( String ) game.get( "score" ); // "3-4 W"
			final boolean hasResult = StringUtils.isNotEmpty( score );
			if ( hasResult ) {
				final String[] split = score.split( " " );
				final String[] points = split[ 0 ].split( "-" );
				remoteGame.setScore1( Integer.parseInt( points[ 0 ] ) );
				remoteGame.setScore2( Integer.parseInt( points[ 1 ] ) );
			}

			final LocalDateTime startTime = LocalDateTime.parse( ( String ) game.get( "startTime" ), DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss" ) );
			remoteGame.setBeginningTime( startTime ); // 2015/05/01 21:30:00
			remoteGame.setHomeTeamNumber( ( ( String ) game.get( "loc" ) ).equals( "home" ) ? 1 : 2 );
			remoteGame.setFinished( hasResult );

			remoteGame.setLoaded( true );

			result.add( remoteGame );
		}

		return result;
	}

	public void setRemoteContentService( final RemoteContentService remoteContentService ) {
		this.remoteContentService = remoteContentService;
	}

	public void setTeamService( final TeamService teamService ) {
		this.teamService = teamService;
	}

	public void setNhlStatisticsServerURLService( final NHLStatisticsServerURLService nhlStatisticsServerURLService ) {
		this.nhlStatisticsServerURLService = nhlStatisticsServerURLService;
	}
}

/* 'ANA', 'BOS', 'BUF', 'CAR', 'CBJ', 'CGY', 'CHI', 'COL', 'DAL', 'DET', 'EDM', 'FLA', 'LAK',
   'MIN', 'MTL', 'NJD', 'NYR', 'OTT', 'PHI', 'PHX', 'PIT', 'SJS', 'STL', 'TBL', 'TOR', 'VAN',
   'WPG',  'WSH'
*/