package totalizator.app.services.matches.imports.strategies.nba;

import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.StatisticsServerURLService;

import java.time.LocalDate;

@Service( "nbaStatisticsServerURLService" )
public class NBAStatisticsServerURLService implements StatisticsServerURLService {

	@Override
	public String remoteGamesIdsURL( final Cup cup, final LocalDate date ) {
		// import by date ( 05/01/2015 mm/dd/yyyy - first of may)
		// http://stats.nba.com/stats/scoreboard?LeagueID=00&gameDate=05/01/2015&DayOffset=0
		return String.format( "http://stats.nba.com/stats/scoreboard?LeagueID=00&gameDate=%s/%s/%s&DayOffset=0", date.getMonthValue(), date.getDayOfMonth(), date.getYear() );
	}

	@Override
	public String loadRemoteGameURL( final String remoteGameId ) {
		// Game ID format: 0021401217
		// 0 		- ?
		// 02 		- league code
		// 14 		- years of cup
		// 0		- ?
		// 1217		- game ID
		return String.format( "http://stats.nba.com/stats/boxscore?GameID=%s&RangeType=0&StartPeriod=0&EndPeriod=0&StartRange=0&EndRange=0", remoteGameId );
	}
}
