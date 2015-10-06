package totalizator.app.services.matches;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class MatchesAndBetsWidgetServiceImpl implements MatchesAndBetsWidgetService {

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public List<Match> loadAll( final MatchesBetSettingsDTO dto ) {

		final List<Match> matches = matchService.loadAll( cupService.load( ( dto.getCupId() ) ) );

		if ( dto.getTeamId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return ( match.getTeam1().getId() == dto.getTeamId() ) || ( match.getTeam2().getId() == dto.getTeamId() );
				}
			} );
		}

		if ( dto.getTeam2Id() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getTeam1().getId() == dto.getTeam2Id() || match.getTeam2().getId() == dto.getTeam2Id();
				}
			} );
		}

		if ( ! dto.isShowFutureMatches() ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return matchService.isMatchFinished( match );
				}
			} );
		}

		if ( ! dto.isShowFinished() ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return ! matchService.isMatchFinished( match );
				}
			} );
		}

		if ( dto.isFilterByDateEnable() ) {

			final String _filterByDate = dto.getFilterByDate();
			final LocalDate filterByDate = dateTimeService.parseDate( _filterByDate );

			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return dateTimeService.hasTheSameDate( match.getBeginningTime(), filterByDate );
				}
			} );
		}

		if ( dto.isShowFutureMatches() ) {
			Collections.reverse( matches );
		}

		return matches;
	}
}
