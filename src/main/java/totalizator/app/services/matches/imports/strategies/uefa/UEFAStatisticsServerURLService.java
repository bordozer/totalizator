package totalizator.app.services.matches.imports.strategies.uefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.services.CategoryService;
import totalizator.app.services.matches.imports.StatisticsServerURLService;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;

@Service( "uefaStatisticsServerURLService" )
public class UEFAStatisticsServerURLService implements StatisticsServerURLService {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public String remoteGamesIdsURL( final Cup cup, final LocalDate date ) {

		final Category category = categoryService.load( cup.getCategory().getId() );

		final LocalDate today = dateTimeService.getNow().toLocalDate();
		final int diffInDays = dateTimeService.diffInDays( today, date );

		final int offset = diffInDays >= 0 ? diffInDays + 1 : Math.abs( diffInDays );

		return String.format( "http://api.football-data.org/alpha/soccerseasons/%s/fixtures?timeFrame=%s%d"
				, category.getImportId()
				, diffInDays < 0 ? "p" : "n"
				, offset
		);
	}

	@Override
	public String loadRemoteGameURL( final String remoteGameId ) {
		return String.format( "http://api.football-data.org/alpha/fixtures/%s", remoteGameId );
	}

	public void setCategoryService( final CategoryService categoryService ) {
		this.categoryService = categoryService;
	}

	public void setDateTimeService( final DateTimeService dateTimeService ) {
		this.dateTimeService = dateTimeService;
	}
}
