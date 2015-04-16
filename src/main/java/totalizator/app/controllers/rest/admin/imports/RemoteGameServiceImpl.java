package totalizator.app.controllers.rest.admin.imports;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.controllers.rest.admin.imports.nba.NBAGame;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RemoteGameServiceImpl implements RemoteGameService {

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public RemoteGame parseGame( final String remoteGameJSON ) {

		final Gson gson = new Gson();


		final NBAGame nbaGame = gson.fromJson( remoteGameJSON, NBAGame.class );

		final LocalDateTime gameTime = getDate( nbaGame );

		final String _team1 = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 5 );
		final int score1 = ( Integer ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 7 );

		final String _team2 = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 10 );
		final int score2 = ( Integer ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 12 );

		final boolean isFinal = ( ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 4 ) ).equals( "Final" );

		final String homeTeamId = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 6 );
		final String team1TeamId = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 3 ).get( "rowSet" ) ).get( 0 ) ).get( 3 );

		final String _homeTeam = team1TeamId.equals( homeTeamId ) ? _team1 : _team2;


		return new RemoteGame();
	}

	private LocalDateTime getDate( final NBAGame nbaGame ) {
		final String _date = ( String ) ( ( ArrayList ) ( ( ArrayList ) nbaGame.getResultSets().get( 0 ).get( "rowSet" ) ).get( 0 ) ).get( 0 );

		return LocalDateTime.parse( _date );
	}
}
