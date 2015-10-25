package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupDao;
import totalizator.app.models.*;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CupServiceImpl implements CupService {

	public static final Comparator<Cup> SORT_BY_CUP_BEGINNING_TIME_COMPARATOR = new Comparator<Cup>() {

		@Override
		public int compare( final Cup o1, final Cup o2 ) {
			return o2.getCupStartTime().compareTo( o1.getCupStartTime() );
		}
	};

	@Autowired
	private CupDao cupRepository;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupTeamService cupTeamService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	@Transactional( readOnly = true )
	public List<Cup> loadAll() {
		return cupRepository.loadAll().stream()
				.sorted( SORT_BY_CUP_BEGINNING_TIME_COMPARATOR )
				.collect( Collectors.toList() );
	}

	@Override
	@Transactional
	public Cup save( final Cup entry ) {
		return cupRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public Cup load( final int id ) {
		return cupRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {

		final Cup cup = load( id );

		cupTeamService.clearForCup( id ); 							// delete assigned teams

		cupWinnerService.deleteAllWinners( cup );					// delete all cup winners

		cupBetsService.clearForCup( cup );							// delete all cup winners user's bets

		final List<Match> matches = matchService.loadAll( cup );	// delete all matches ( and bets )
		for ( final Match match : matches ) {
			matchService.delete( match.getId() );
		}

		cupRepository.delete( id );									// delete the cup
	}

	@Override
	@Transactional( readOnly = true )
	public Cup findByName( final String name ) {
		return cupRepository.findByName( name );
	}

	@Override
	@Transactional( readOnly = true )
	public List<Cup> loadAllCurrent() {
		return loadAll()
				.stream()
				.filter( isCupCurrent() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublic() {
		return loadAll()
				.stream()
				.filter( isCupPublic() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublicCurrent() {
		return loadPublic()
				.stream()
				.filter( isCupCurrent() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublicFinished() {
		return loadPublic()
				.stream()
				.filter( isCupFinished() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> load( final Category category ) {
		return loadAll()
				.stream()
				.filter( forCategory( category ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublic( final Category category ) {
		return loadPublic()
				.stream()
				.filter( forCategory( category ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublicCurrent( final Category category ) {
		return loadPublicCurrent()
				.stream()
				.filter( forCategory( category ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublicCurrent( final SportKind sportKind ) {
		return loadPublicCurrent()
				.stream()
				.filter( forSportKind( sportKind ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublicFinished( final Category category ) {
		return loadPublicFinished()
				.stream()
				.filter( forCategory( category ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadPublic( final SportKind sportKind ) {
		return loadPublic()
				.stream()
				.filter( forSportKind( sportKind ) )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadHidden() {
		return loadAll()
				.stream()
				.filter( isCupHidden() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadHiddenCurrent() {
		return loadHidden()
				.stream()
				.filter( isCupCurrent() )
				.collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadCups( final PointsCalculationStrategy strategy ) {
		return cupRepository.loadCups( strategy );
	}

	@Override
	@Transactional
	public Cup save( final Cup cup, final List<CupWinner> winners ) {

		final Cup saved = save( cup );

		cupWinnerService.deleteAllWinners( cup );

		cupWinnerService.saveAll( winners );

		return saved;
	}

	@Override
	public boolean isCupStarted( final Cup cup ) {
		return dateTimeService.getNow().isAfter( cup.getCupStartTime() );
	}

	@Override
	public boolean isCupFinished( final Cup cup ) {
		return cupWinnerService.hasChampions( cup );
	}

	@Override
	public boolean isCupPublic( final Cup cup ) {
		return isCupPublic().test( cup );
	}

	@Override
	public Comparator<Cup> categoryNameOrCupNameComparator() {

		return new Comparator<Cup>() {

			@Override
			public int compare( final Cup cup1, final Cup cup2 ) {

				final Category category1 = cup1.getCategory();
				final Category category2 = cup2.getCategory();

				if ( category1.getSportKind().equals( category2.getSportKind() ) ) {
					return category1.getCategoryName().compareToIgnoreCase( category2.getCategoryName() );
				}

				return category1.getSportKind().getSportKindName().compareToIgnoreCase( category2.getSportKind().getSportKindName() );
			}
		};
	}

	private Predicate<Cup> isCupCurrent() {

		return new Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {
				return ! isCupFinished( cup );
			}
		};
	}

	private Predicate<Cup> isCupPublic() {

		return new Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {
				return cup.isPublicCup();
			}
		};
	}

	private Predicate<Cup> isCupFinished() {
		return isCupCurrent().negate();
	}

	private Predicate<Cup> isCupHidden() {
		return isCupPublic().negate();
	}

	private Predicate<Cup> forCategory( final Category category ) {
		return new Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {
				return cup.getCategory().equals( category );
			}
		};
	}

	private Predicate<Cup> forSportKind( final SportKind sportKind ) {
		return new Predicate<Cup>() {
			@Override
			public boolean test( final Cup cup ) {
				return cup.getCategory().getSportKind().equals( sportKind );
			}
		};
	}
}
