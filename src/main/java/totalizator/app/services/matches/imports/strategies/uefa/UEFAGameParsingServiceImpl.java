package totalizator.app.services.matches.imports.strategies.uefa;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.remote.RemoteContentService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.collect.Sets.newTreeSet;

@Service( value = "uefaGameParsingService" )
public class UEFAGameParsingServiceImpl implements RemoteGameParsingService {

	@Autowired
	private RemoteContentService remoteContentService;

	@Override
	public Set<String> extractRemoteGameIds( final String remoteGameJSON ) {

		final Gson gson = new Gson();

		final LinkedTreeMap uefaGamesJSON = ( LinkedTreeMap ) gson.fromJson( remoteGameJSON, Object.class );

		if ( uefaGamesJSON == null ) {
			return newTreeSet();
		}

		final TreeSet<String> result = newTreeSet();

		final List fixtures = ( ArrayList ) uefaGamesJSON.get( "fixtures" );
		for ( final Object fixture : fixtures ) {
			final LinkedTreeMap links = ( LinkedTreeMap ) ( ( LinkedTreeMap ) fixture ).get( "_links" );
			final LinkedTreeMap self = ( LinkedTreeMap ) links.get( "self" );
			final String href = ( String ) self.get( "href" );

			final String[] strings = href.split( "/" );
			final String remoteGameId = strings[ strings.length - 1 ];
			result.add( remoteGameId );
		}

		return result;
	}

	@Override
	public RemoteGame parseGame( final String remoteGameId, final String remoteGameJSON ) throws IOException {

		final Gson gson = new Gson();
		final LinkedTreeMap fixture = ( LinkedTreeMap ) ( ( LinkedTreeMap ) gson.fromJson( remoteGameJSON, Object.class ) ).get( "fixture" );

		final String team1Name = ( String ) fixture.get( "homeTeamName" );
		final String team2Name = ( String ) fixture.get( "awayTeamName" );

		final String date = ( String ) fixture.get( "date" ); // 2015-09-12T14:00:00Z // TODO: timezone
		final LocalDateTime beginningTime = LocalDateTime.parse( date.substring( 0, date.length() - 1 ) );

		final LinkedTreeMap scores = ( LinkedTreeMap ) fixture.get( "result" );
		final Double score1 = ( Double ) scores.get( "goalsHomeTeam" );
		final Double score2 = ( Double ) scores.get( "goalsAwayTeam" );

		final String status = ( String ) ( ( LinkedTreeMap ) fixture ).get( "status" );

		final RemoteGame result = new RemoteGame( remoteGameId );

		result.setRemoteTeam1Id( team1Name );
		result.setRemoteTeam1Name( team1Name );

		result.setRemoteTeam2Id( team2Name );
		result.setHomeTeamNumber( 1 );
		result.setRemoteTeam2Name( team2Name );

		result.setBeginningTime( beginningTime );

		result.setScore1( score1 < 0 ? 0 : ( int ) ( double ) score1 );
		result.setScore2( score2 < 0 ? 0 : ( int ) ( double ) score2 );

		result.setFinished( ! status.equals( "TIMED" ) );

		return result;
	}

	/*private String getRemoteTeamIdByName( final String teamName, String categoryImportId ) throws IOException {

		final RemoteServerRequest request = new RemoteServerRequest( String.format( "http://api.football-data.org/alpha/soccerseasons/%s/teams", categoryImportId ) );
		request.setxAuthToken( UEFAStatisticsAPIService.X_AUTH_TOKEN );

		final String remoteTeamsContent = remoteContentService.getRemoteContent( request );
		// TODO: cache this instead of hardcoding
//		final String remoteTeamsContent = "{\"_links\":[{\"self\":\"http://api.football-data.org/alpha/soccerseasons/398/teams\"},{\"soccerseason\":\"http://api.football-data.org/alpha/soccerseasons/398\"}],\"count\":20,\"teams\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/66\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/66/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/66/players\"}},\"name\":\"Manchester United FC\",\"code\":\"MUFC\",\"shortName\":\"ManU\",\"squadMarketValue\":\"377,250,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/d/da/Manchester_United_FC.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/73\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/73/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/73/players\"}},\"name\":\"Tottenham Hotspur FC\",\"code\":\"THFC\",\"shortName\":\"Spurs\",\"squadMarketValue\":\"288,000,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/b/b4/Tottenham_Hotspur.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/1044\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/1044/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/1044/players\"}},\"name\":\"AFC Bournemouth\",\"code\":\"AFCB\",\"shortName\":\"Bournemouth\",\"squadMarketValue\":\"3,545,000 €\",\"crestUrl\":\"https://upload.wikimedia.org/wikipedia/de/4/41/Afc_bournemouth.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/58\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/58/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/58/players\"}},\"name\":\"Aston Villa FC\",\"code\":\"AVFC\",\"shortName\":\"Aston Villa\",\"squadMarketValue\":\"106,050,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/9/9f/Aston_Villa_logo.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/62\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/62/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/62/players\"}},\"name\":\"Everton FC\",\"code\":\"EFC\",\"shortName\":\"Everton\",\"squadMarketValue\":\"194,300,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/f/f9/Everton_FC.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/346\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/346/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/346/players\"}},\"name\":\"Watford FC\",\"code\":\"Watfordfc\",\"shortName\":\"Watford\",\"squadMarketValue\":\"84,000,000 €\",\"crestUrl\":\"https://upload.wikimedia.org/wikipedia/en/e/e2/Watford.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/338\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/338/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/338/players\"}},\"name\":\"Leicester City FC\",\"code\":\"LCFC\",\"shortName\":\"Foxes\",\"squadMarketValue\":\"89,800,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/en/6/63/Leicester02.png\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/71\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/71/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/71/players\"}},\"name\":\"Sunderland AFC\",\"code\":\"SUN\",\"shortName\":\"Sunderland\",\"squadMarketValue\":\"113,250,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/6/60/AFC_Sunderland.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/68\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/68/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/68/players\"}},\"name\":\"Norwich City FC\",\"code\":\"NCFC\",\"shortName\":\"Norwich\",\"squadMarketValue\":\"72,650,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/8/8c/Norwich_City.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/354\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/354/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/354/players\"}},\"name\":\"Crystal Palace FC\",\"code\":\"CRY\",\"shortName\":\"Crystal\",\"squadMarketValue\":\"116,750,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/b/bf/Crystal_Palace_F.C._logo_(2013).png\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/61\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/61/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/61/players\"}},\"name\":\"Chelsea FC\",\"code\":\"CFC\",\"shortName\":\"Chelsea\",\"squadMarketValue\":\"531,750,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/5/5c/Chelsea_crest.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/72\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/72/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/72/players\"}},\"name\":\"Swansea City FC\",\"code\":\"SWA\",\"shortName\":\"Swans\",\"squadMarketValue\":\"129,250,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/a/ab/Swansea_City_Logo.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/67\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/67/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/67/players\"}},\"name\":\"Newcastle United FC\",\"code\":\"NUFC\",\"shortName\":\"Newcastle\",\"squadMarketValue\":\"157,500,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/5/56/Newcastle_United_Logo.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/340\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/340/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/340/players\"}},\"name\":\"Southampton FC\",\"code\":\"SFC\",\"shortName\":\"Southampton\",\"squadMarketValue\":\"185,750,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/c/c9/FC_Southampton.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/57\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/57/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/57/players\"}},\"name\":\"Arsenal FC\",\"code\":\"AFC\",\"shortName\":\"Arsenal\",\"squadMarketValue\":\"402,000,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/en/5/53/Arsenal_FC.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/563\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/563/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/563/players\"}},\"name\":\"West Ham United FC\",\"code\":\"WHU\",\"shortName\":\"West Ham\",\"squadMarketValue\":\"165,000,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/e/e0/West_Ham_United_FC.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/70\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/70/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/70/players\"}},\"name\":\"Stoke City FC\",\"code\":\"SCFC\",\"shortName\":\"Stoke\",\"squadMarketValue\":\"116,250,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/a/a3/Stoke_City.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/64\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/64/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/64/players\"}},\"name\":\"Liverpool FC\",\"code\":\"LFC\",\"shortName\":\"Liverpool\",\"squadMarketValue\":\"330,250,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/0/0a/FC_Liverpool.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/74\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/74/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/74/players\"}},\"name\":\"West Bromwich Albion FC\",\"code\":\"WBA\",\"shortName\":\"West Bromwich\",\"squadMarketValue\":\"93,500,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/8/8b/West_Bromwich_Albion.svg\"},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"},\"fixtures\":{\"href\":\"http://api.football-data.org/alpha/teams/65/fixtures\"},\"players\":{\"href\":\"http://api.football-data.org/alpha/teams/65/players\"}},\"name\":\"Manchester City FC\",\"code\":\"MCFC\",\"shortName\":\"ManCity\",\"squadMarketValue\":\"480,850,000 €\",\"crestUrl\":\"http://upload.wikimedia.org/wikipedia/de/f/fd/ManCity.svg\"}]}";

		final Gson gson = new Gson();
		final List teams = ( List ) ( ( LinkedTreeMap ) gson.fromJson( remoteTeamsContent, Object.class ) ).get( "teams" );
		for ( final Object _team : teams ) {
			final LinkedTreeMap team = ( LinkedTreeMap ) _team;
			if ( team.get( "name" ).equals( teamName ) ) {
				return ( String ) team.get( "code" );
			}
		}

		return null;
	}*/
}
