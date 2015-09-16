package totalizator.app.services.matches.imports.strategies.uefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.matches.imports.StatisticsServerURLService;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;

@Service( "uefaStatisticsServerURLService" )
public class UEFAStatisticsServerURLService implements StatisticsServerURLService {

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public String remoteGamesIdsURL( final Cup cup, final LocalDate date ) {

		final LocalDate today = dateTimeService.getNow().toLocalDate();
		final int diffInDays = dateTimeService.diffInDays( today, date );

		final int offset = diffInDays >= 0 ? diffInDays + 1 : Math.abs( diffInDays );

		return String.format( "http://api.football-data.org/alpha/soccerseasons/%s/fixtures?timeFrame=%s%d"
				, cup.getCupImportId()
				, diffInDays < 0 ? "p" : "n"
				, offset
		);
	}

	@Override
	public String loadRemoteGameURL( final String remoteGameId ) {
		return String.format( "http://api.football-data.org/alpha/fixtures/%s", remoteGameId );
	}

	public void setDateTimeService( final DateTimeService dateTimeService ) {
		this.dateTimeService = dateTimeService;
	}
}

/* See http://api.football-data.org/documentation

	http://api.football-data.org/alpha/soccerseasons/?season=2014

	http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/teams
	http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures

	http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?matchday=5
	http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?timeFrame=p13
	http://api.football-data.org/alpha/soccerseasons/{soccerseasons}/fixtures?timeFrame=n1

	http://api.football-data.org/alpha/fixtures/{Game ID}

*/