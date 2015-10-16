package totalizator.app.services.points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserSummaryPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.points.calculation.cup.UserCupWinnersBonusCalculationService;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupPointsServiceImpl implements CupPointsService {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchPointsService matchPointsService;

	@Autowired
	private UserCupWinnersBonusCalculationService userCupWinnersBonusCalculationService;

	@Autowired
	private UserGroupService userGroupService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserCupPointsHolder> getUsersCupPoints( final Cup cup ) {
		return cupPointsByUser( cup, userService.loadAll(), null );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup ) {
		return cupPointsByUser( cup, userGroupService.loadUserGroupMembers( userGroup ), userGroup );
	}

	private List<UserCupPointsHolder> cupPointsByUser( final Cup cup, final List<User> users, final UserGroup userGroup ) {

		final List<UserCupPointsHolder> result = newArrayList();

		return users
				.stream()
				.map( new Function<User, UserCupPointsHolder>() {
					@Override
					public UserCupPointsHolder apply( final User user ) {

						final UserSummaryPointsHolder pointsHolder = userGroup == null ? matchPointsService.load( user, cup ) : matchPointsService.load( user, cup, userGroup );

						if ( pointsHolder == null ) {
							return null;
						}

						final UserCupPointsHolder holder = new UserCupPointsHolder( user, cup );
						holder.setMatchBetPoints( pointsHolder.getBetPoints() );
						holder.setMatchBonuses( pointsHolder.getMatchBonus() );
						holder.setCupWinnerBonus( userCupWinnersBonusCalculationService.getUserCupWinnersSummaryPoints( cup, user ) );

						return holder;
					}
				} )
				.filter( new Predicate<UserCupPointsHolder>() {
					@Override
					public boolean test( final UserCupPointsHolder userCupPointsHolder ) {
						return userCupPointsHolder != null;
					}
				} )
				.sorted( new Comparator<UserCupPointsHolder>() {
					@Override
					public int compare( final UserCupPointsHolder o1, final UserCupPointsHolder o2 ) {

						final float points1 = o1.getPoints();
						final float points2 = o2.getPoints();

						if ( points1 != points2 ) {
							return ( ( Float ) points2 ).compareTo( points1 );
						}

						return o1.getUser().getUsername().compareToIgnoreCase( o2.getUser().getUsername() );
					}
				} )
				.collect( Collectors.toList() );
	}
}
