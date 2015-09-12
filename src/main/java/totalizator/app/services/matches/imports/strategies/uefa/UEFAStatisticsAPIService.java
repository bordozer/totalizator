package totalizator.app.services.matches.imports.strategies.uefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.services.CategoryService;
import totalizator.app.services.remote.RemoteContentService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameParsingService;
import totalizator.app.services.matches.imports.strategies.StatisticsServerService;
import totalizator.app.services.remote.RemoteServerRequest;
import totalizator.app.services.utils.DateTimeService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class UEFAStatisticsAPIService implements StatisticsServerService {

	public static final String X_AUTH_TOKEN = "a1ce133492c54f0a9aa03ac7242eedeb"; // TODO: move to properties?

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private RemoteContentService remoteContentService;

	@Autowired
	private RemoteGameParsingService uefaGameParsingService;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public Collection<String> loadRemoteGameIdsOnDate( final Cup cup, final LocalDate date ) throws IOException {

		final Category category = categoryService.load( cup.getCategory().getId() );

		final LocalDate today = dateTimeService.getNow().toLocalDate();
		final int diffInDays = dateTimeService.diffInDays( today, date );

		final int offset = diffInDays >= 0 ? diffInDays + 1 : Math.abs( diffInDays );

		final String url = String.format( "http://api.football-data.org/alpha/soccerseasons/%s/fixtures?timeFrame=%s%d"
				, category.getImportId()
				, diffInDays < 0 ? "p" : "n"
				, offset
		);

		final RemoteServerRequest request = new RemoteServerRequest( url );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteContent = remoteContentService.getRemoteContent( request );
		// TODO: mock!!!
//		final String remoteContent = "{\"_links\":[{\"self\":\"http://api.football-data.org/alpha/soccerseasons/398/fixtures\"},{\"soccerseason\":\"http://api.football-data.org/alpha/soccerseasons/398\"}],\"count\":7,\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147037\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/62\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/61\"}},\"date\":\"2015-09-12T11:45:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Everton FC\",\"awayTeamName\":\"Chelsea FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147042\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/74\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/340\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"West Bromwich Albion FC\",\"awayTeamName\":\"Southampton FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147041\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/346\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/72\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Watford FC\",\"awayTeamName\":\"Swansea City FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147035\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/57\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/70\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Arsenal FC\",\"awayTeamName\":\"Stoke City FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147113\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/68\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/1044\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Norwich City FC\",\"awayTeamName\":\"AFC Bournemouth\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147036\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/354\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/65\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Crystal Palace FC\",\"awayTeamName\":\"Manchester City FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147039\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/66\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/64\"}},\"date\":\"2015-09-12T16:30:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Manchester United FC\",\"awayTeamName\":\"Liverpool FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}}]}";

		return uefaGameParsingService.extractRemoteGameIds( remoteContent );
	}

	@Override
	public RemoteGame loadRemoteGame( final String remoteGameId ) throws IOException {

		final String url = String.format( "http://api.football-data.org/alpha/fixtures/%s", remoteGameId );

		final RemoteServerRequest request = new RemoteServerRequest( url );
		request.setxAuthToken( X_AUTH_TOKEN );

		final String remoteGameJSON = remoteContentService.getRemoteContent( request );
		// TODO: mock!!!
//		final String remoteGameJSON = "{\"fixture\":{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/alpha/fixtures/147041\"},\"soccerseason\":{\"href\":\"http://api.football-data.org/alpha/soccerseasons/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/346\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/alpha/teams/72\"}},\"date\":\"2015-09-12T14:00:00Z\",\"status\":\"TIMED\",\"matchday\":5,\"homeTeamName\":\"Watford FC\",\"awayTeamName\":\"Swansea City FC\",\"result\":{\"goalsHomeTeam\":-1,\"goalsAwayTeam\":-1}},\"head2head\":{\"count\":null,\"timeFrameStart\":null,\"timeFrameEnd\":null,\"homeTeamWins\":null,\"awayTeamWins\":null,\"draws\":null,\"lastHomeWinHomeTeam\":null,\"lastWinHomeTeam\":null,\"lastAwayWinAwayTeam\":null,\"lastWinAwayTeam\":null,\"fixtures\":[]}}";

		return uefaGameParsingService.parseGame( remoteGameId, remoteGameJSON );
	}
}

// See http://api.football-data.org/documentation

//http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/teams
//http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures

//http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?matchday=5
//http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?timeFrame=p13
//http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?timeFrame=n1

//http://api.football-data.org/alpha/fixtures/{Game ID}