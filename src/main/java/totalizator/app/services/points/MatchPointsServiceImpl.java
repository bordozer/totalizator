package totalizator.app.services.points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.points.UserSummaryPointsHolder;
import totalizator.app.dao.MatchPointsDao;
import totalizator.app.dao.MatchSummaryPoints;
import totalizator.app.models.*;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

@Service
public class MatchPointsServiceImpl implements MatchPointsService {

	@Autowired
	private MatchPointsDao matchPointsRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	public List<MatchPoints> loadAll() {
		return matchPointsRepository.loadAll();
	}

	@Override
	public MatchPoints load( final int id ) {
		return matchPointsRepository.load( id );
	}

	@Override
	@Transactional
	public MatchPoints save( final MatchPoints entry ) {
		return matchPointsRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		matchPointsRepository.delete( id );
	}

	@Override
	@Transactional
	public void delete( final Match match ) {
		matchPointsRepository.delete( match );
	}

	@Override
	@Transactional
	public void delete( final Cup cup ) {
		matchPointsRepository.delete( cup );
	}

	@Override
	@Transactional
	public void delete( final UserGroup userGroup ) {
		matchPointsRepository.delete( userGroup );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<MatchPoints> loadAll( final Match match ) {
		return matchPointsRepository.loadAll( match );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<MatchPoints> loadAll( final Match match, final UserGroup userGroup ) {
		return matchPointsRepository.loadAll( match, userGroup );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public MatchPoints load( final User user, final Match match ) {
		return matchPointsRepository.load( user, match );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public MatchPoints load( final User user, final Match match, final UserGroup userGroup ) {
		return matchPointsRepository.load( user, match, userGroup );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public UserSummaryPointsHolder load( final User user, final Cup cup ) {

		final MatchSummaryPoints summaryPoints = matchPointsRepository.loadSummary( user, cup );

		if ( summaryPoints == null ) {
			return null;
		}

		final int betPoints = summaryPoints.getMatchPoints();
		final float matchBonus = summaryPoints.getMatchBonus();

		return new UserSummaryPointsHolder( user, betPoints, matchBonus );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public UserSummaryPointsHolder load( final User user, final Cup cup, final UserGroup userGroup ) {

		final MatchSummaryPoints summaryPoints = matchPointsRepository.loadSummary( user, cup, userGroup );

		if ( summaryPoints == null ) {
			return null;
		}

		final int betPoints = summaryPoints.getMatchPoints();
		final float matchBonus = summaryPoints.getMatchBonus();

		return new UserSummaryPointsHolder( user, betPoints, matchBonus );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserSummaryPointsHolder> getUsersRating( final LocalDate date ) {
		return getUserSummaryPointsHolders( matchPointsRepository.loadAll( dateTimeService.getFirstSecondOf( date ), dateTimeService.getLastSecondOf( date ) ) );
	}

	@Override
	public UserSummaryPointsHolder getUserRating( final User user, final Cup cup, final LocalDate date ) {

		final MatchSummaryPoints matchSummaryPoints = matchPointsRepository.loadSummary( user, cup, dateTimeService.getFirstSecondOf( date ), dateTimeService.getLastSecondOf( date ) );

		if ( matchSummaryPoints == null ) {
			return null;
		}

		return new UserSummaryPointsHolder( user, matchSummaryPoints.getMatchPoints(), matchSummaryPoints.getMatchBonus() );
	}

	@Override
	public UserSummaryPointsHolder getUserRating( final User user, final Cup cup, final LocalDate dateFrom, final LocalDate dateTo ) {

		final MatchSummaryPoints matchSummaryPoints = matchPointsRepository.loadSummary( user, cup, dateTimeService.getFirstSecondOf( dateFrom ), dateTimeService.getLastSecondOf( dateTo ) );

		if ( matchSummaryPoints == null ) {
			return null;
		}

		return new UserSummaryPointsHolder( user, matchSummaryPoints.getMatchPoints(), matchSummaryPoints.getMatchBonus() );
	}

	private List<UserSummaryPointsHolder> getUserSummaryPointsHolders( final List<MatchPoints> matchPoints ) {

		final Map<User, List<MatchPoints>> matchPointsByUserMap = matchPoints
				.stream()
				.collect( Collectors.groupingBy( MatchPoints::getUser ) );

		final Map<User, UserSummaryPointsHolder> pointsHolderMap = newHashMap();

		matchPointsByUserMap
				.keySet()
				.stream()
				.forEach( user -> {

					final List<MatchPoints> userMatchPoints = matchPointsByUserMap.get( user );

					final double matchBetPoints = userMatchPoints.stream().collect( Collectors.summingDouble( MatchPoints::getMatchPoints ) );
					final double matchBonuses = userMatchPoints.stream().collect( Collectors.summingDouble( MatchPoints::getMatchBonus ) );

					pointsHolderMap.put( user, new UserSummaryPointsHolder( user, ( int ) matchBetPoints, ( float ) matchBonuses ) );
				} );

		return pointsHolderMap
				.keySet()
				.stream()
				.map( new Function<User, UserSummaryPointsHolder>() {
					@Override
					public UserSummaryPointsHolder apply( final User user ) {
						return pointsHolderMap.get( user );
					}
				} )
				.sorted( new Comparator<UserSummaryPointsHolder>() {
					@Override
					public int compare( final UserSummaryPointsHolder o1, final UserSummaryPointsHolder o2 ) {
						return new Float( ( float ) o2.getBetPoints() + o2.getMatchBonus() ).compareTo( ( float ) o1.getBetPoints() + o1.getMatchBonus() );
					}
				} )
				.collect( Collectors.toList() );
	}
}
