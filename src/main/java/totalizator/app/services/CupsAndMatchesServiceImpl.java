package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.services.matches.MatchService;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CupsAndMatchesServiceImpl implements CupsAndMatchesService {

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupService cupService;

	@Override
	public List<Cup> getCupsHaveMatchesOnDate( final LocalDate date ) {

		final List<Match> matchesToday = matchService.loadAllOnDate( date );

		return matchesToday
				.stream()
				.filter( new Predicate<Match>() {
					@Override
					public boolean test( final Match match ) {
						return cupService.isCupPublic( match.getCup() );
					}
				} )
				.map( new Function<Match, Cup>() {
					@Override
					public Cup apply( final Match match ) {
						return match.getCup();
					}
				} )
				.distinct()
				.collect( Collectors.toList() );
	}
}
