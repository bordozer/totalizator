package totalizator.app.services.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.utils.DateTimeService;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

		List<Match> matches = getMatches( dto );

		if ( ! dto.isShowFutureMatches() ) {
			matches = filterOutNotFinished( matches );
		}

		if ( ! dto.isShowFinished() ) {
			matches = filterOutFinished( matches );
		}

		if ( dto.getTeamId() > 0 ) {
			matches = filterByTeam( matches, dto.getTeamId() );
		}

		if ( dto.getTeam2Id() > 0 ) {
			matches = filterByTeam( matches, dto.getTeam2Id() );
		}

		if ( dto.isShowFutureMatches() ) {
			matches = sortByBeginningTimeAsc( matches );
		}

		if ( ! dto.isShowFutureMatches() ) {
			matches = sortByBeginningTimeDesc( matches );
		}

		return matches;
	}

	private List<Match> getMatches( final MatchesBetSettingsDTO dto ) {

		if ( dto.isFilterByDateEnable() ) {
			return matchService.loadAllOnDate( dto.getCupId(), dateTimeService.parseDate( dto.getFilterByDate() ) );
		}

		final boolean showNotFinishedMatchesOnly = dto.isShowFutureMatches() && ! dto.isShowFinished();
		if ( showNotFinishedMatchesOnly ) {
			return matchService.loadAllNotFinished( dto.getCupId() );
		}

		if ( dto.getTeamId() > 0 && dto.getTeam2Id() > 0 ) {
			return matchService.loadAll( dto.getCupId(), dto.getTeamId(), dto.getTeam2Id() );
		}

		if ( dto.getTeamId() > 0 ) {
			return matchService.loadAll( dto.getCupId(), dto.getTeamId() );
		}

		if ( dto.getTeam2Id() > 0 ) {
			return matchService.loadAll( dto.getCupId(), dto.getTeam2Id() );
		}

		return matchService.loadAll( cupService.load( ( dto.getCupId() ) ) ); // TODO: paging here
	}

	private List<Match> filterByTeam( final List<Match> matches, final int teamId ) {

		return matches
				.stream()
				.filter( filterByTeamPredicate( teamId ) )
				.collect( Collectors.toList() );
	}

	private List<Match> filterOutNotFinished( final List<Match> matches ) {

		return matches
				.stream()
				.filter( matchFinishedPredicate() )
				.collect( Collectors.toList() );
	}

	private List<Match> filterOutFinished( final List<Match> matches ) {

		return matches
				.stream()
				.filter( matchFinishedPredicate().negate() )
				.collect( Collectors.toList() );
	}

	private List<Match> sortByBeginningTimeAsc( final List<Match> matches ) {

		return  matches
				.stream()
				.sorted( beginningTimeComparator().reversed() )
				.collect( Collectors.toList() );
	}

	private List<Match> sortByBeginningTimeDesc( List<Match> matches ) {

		return  matches
				.stream()
				.sorted( beginningTimeComparator() )
				.collect( Collectors.toList() );
	}

	private Predicate<Match> filterByTeamPredicate( final int teamId ) {

		return new Predicate<Match>() {
			@Override
			public boolean test( final Match match ) {
				return ( match.getTeam1().getId() == teamId ) || ( match.getTeam2().getId() == teamId );
			}
		};
	}

	private Predicate<Match> matchFinishedPredicate() {

		return new Predicate<Match>() {
			@Override
			public boolean test( final Match match ) {
				return matchService.isMatchFinished( match );
			}
		};
	}

	private Comparator<Match> beginningTimeComparator() {
		return new Comparator<Match>() {
			@Override
			public int compare( final Match o1, final Match o2 ) {
				return o2.getBeginningTime().compareTo( o1.getBeginningTime() );
			}
		};
	}
}
