package totalizator.app.controllers.rest.admin.imports.nba;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.imports.RemoteGame;
import totalizator.app.controllers.rest.admin.imports.RemoteGameService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class NBAGameServiceImpl implements RemoteGameService {

	@Override
	public RemoteGame parseGame( final String remoteGameJSON ) {

		final Gson gson = new Gson();

		final NBAGame nbaGame = gson.fromJson( remoteGameJSON, NBAGame.class );

		final LocalDateTime gameTime = getDate( nbaGame );

		final String _team1City = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 4 );
		final String _team1 = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 5 );
		final Double score1 = ( Double ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 1 ).get( "rowSet" ) ).get( 0 ) ).get( 21 );

		final String _team2City = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 9 );
		final String _team2 = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 10 );
		final Double score2 = ( Double ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 1 ).get( "rowSet" ) ).get( 1 ) ).get( 21 );

		final boolean isFinal = ( ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 4 ) ).equals( "Final" );

		final Double homeTeamId = ( Double ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 6 );
		final Double team1TeamId = ( Double ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 3 );

		final String _homeTeam = team1TeamId.doubleValue() == homeTeamId.doubleValue() ? _team1 : _team2;
		final int homeTeamNumber = team1TeamId.doubleValue() == homeTeamId.doubleValue() ? 1 : 2;

		final RemoteGame remoteGame = new RemoteGame();

		remoteGame.setGameDate( gameTime );

		remoteGame.setTeam1Name( String.format( "%s %s", _team1City, _team1 ) );
		remoteGame.setScore1( score1.intValue() );

		remoteGame.setTeam2Name( String.format( "%s %s", _team2City, _team2 ) );
		remoteGame.setScore2( score2.intValue() );

		remoteGame.setHomeTeamName( _homeTeam );

		remoteGame.setFinished( isFinal );
		remoteGame.setHomeTeamNumber( homeTeamNumber  );

		return remoteGame;
	}

	private LocalDateTime getDate( final NBAGame nbaGame ) {
		final String _date = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 0 );

		return LocalDateTime.parse( _date );
	}
}
