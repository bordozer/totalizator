package totalizator.app.services.points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.*;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.points.cup.UserCupWinnersBonusCalculationService;
import totalizator.app.services.points.match.bonus.MatchBonusPointsCalculationService;
import totalizator.app.services.points.match.points.UserMatchBetPointsCalculationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

@Service
public class UserMatchPointsCalculationServiceImpl implements UserMatchPointsCalculationService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

	@Autowired
	private MatchBonusPointsCalculationService matchBonusPointsCalculationService;

	@Autowired
	private UserCupWinnersBonusCalculationService userCupWinnersBonusCalculationService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public UserMatchPointsHolder getUserMatchPoints( final Match match, final User user ) {

		final MatchBet matchBet = matchBetsService.load( user, match );
		if ( matchBet != null ) {
			return new UserMatchPointsHolder( userMatchBetPointsCalculationService.getUserMatchBetPoints( matchBet ), getUserMatchBonus( matchBet ) );
		}

		return null;
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public UserMatchPointsHolder getUserMatchPoints( final Match match, final User user, final UserGroup userGroup ) {
		final MatchBet userMatchBet = matchBetsService.load( user, match );
		return new UserMatchPointsHolder( userMatchBetPointsCalculationService.getUserMatchBetPoints( userMatchBet ), getUserMatchBonus( userMatchBet, userGroup ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserCupPointsHolder> getUsersCupPoints( final Cup cup ) {
		final List<User> users = userService.loadAll();
		return sumPointsByUser( cup, userMatchBetPointsCalculationService.getUsersMatchBetsPointHolders( cup, users ), users );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup ) {
		final List<User> users = userGroupService.loadUserGroupMembers( userGroup );
		return sumPointsByUser( cup, userMatchBetPointsCalculationService.getUsersMatchBetsPointHolders( cup, users ), users );
	}

	private List<UserCupPointsHolder> sumPointsByUser( final Cup cup, final List<UserMatchBetPointsHolder> userMatchBetPointsHolders, final List<User> users ) {

		final List<UserMatchPointsHolder> matchPoints = newArrayList();
		matchPoints.addAll(
				userMatchBetPointsHolders
						.stream()
						.map( usersMatchBetsPoint -> new UserMatchPointsHolder( usersMatchBetsPoint, getUserMatchBonus( usersMatchBetsPoint.getMatchBet() ) ) )
						.collect( Collectors.toList() )
		);

		final Map<User, List<UserMatchPointsHolder>> matchPointsByUsers = matchPoints
				.stream()
				.collect( Collectors.groupingBy( UserMatchPointsHolder::getUser ) );

		final Map<User, PointsHolder> pointsHolderMap = newHashMap();
		matchPointsByUsers
				.keySet()
				.stream()
				.forEach( user -> {

					final List<UserMatchPointsHolder> userMatchPoints = matchPointsByUsers.get( user );

					final PointsHolder holder = new PointsHolder();
					holder.matchBetPoints = userMatchPoints.stream().collect( Collectors.summingDouble( UserMatchPointsHolder::getUserBetPoints ) );
					holder.matchBonuses = userMatchPoints.stream().collect( Collectors.summingDouble( UserMatchPointsHolder::getMatchBonus ) );
					holder.cupWinnerBonus = userCupWinnersBonusCalculationService.getUserCupWinnersSummaryPoints( cup, user );

					pointsHolderMap.put( user, holder );
				} );

		final List<UserCupPointsHolder> userCupPointsHolders = newArrayList();
		pointsHolderMap
				.keySet()
				.stream()
				.forEach( user -> {

					final PointsHolder pointsHolder = pointsHolderMap.get( user );

					final UserCupPointsHolder userCupPointsHolder = new UserCupPointsHolder( user, cup );
					userCupPointsHolder.setMatchBetPoints( ( int ) pointsHolder.matchBetPoints );
					userCupPointsHolder.setMatchBonuses( ( float ) pointsHolder.matchBonuses );
					userCupPointsHolder.setCupWinnerBonus( ( int ) pointsHolder.cupWinnerBonus );

					userCupPointsHolders.add( userCupPointsHolder );
				} );

		return userCupPointsHolders
				.stream()
				.sorted(
						( o1, o2 ) -> {

							final int pointsComparator = ( ( Float ) ( o2.getMatchBetPoints() + o2.getMatchBonuses() + o2.getCupWinnerBonus() ) )
									.compareTo( ( Float ) ( o1.getMatchBetPoints() + o1.getMatchBonuses() + o1.getCupWinnerBonus() ) );

							if ( pointsComparator != 0 ) {
								return pointsComparator;
							}

							return o1.getUser().getUsername().compareToIgnoreCase( o2.getUser().getUsername() );
						}
				)
				.collect( Collectors.toList() )
				;
	}

	private float getUserMatchBonus( final MatchBet matchBet ) {
		return getUserMatchBonus( matchBet, null ); // I'm a bad boy
	}

	private float getUserMatchBonus( final MatchBet matchBet, final UserGroup userGroup ) {

		if ( userMatchBetPointsCalculationService.getUserMatchBetPoints( matchBet ).getPoints() < 0 ) {
			return 0; // user who made the bet is loser - no bonuses
		}

		if ( userGroup == null ) {
			return matchBonusPointsCalculationService.calculateMatchBonus( matchBet.getMatch() );
		}

		return matchBonusPointsCalculationService.calculateMatchBonus( matchBet.getMatch(), userGroup );
	}

	public void setUserService( final UserService userService ) {
		this.userService = userService;
	}

	private class PointsHolder {

		double matchBetPoints;
		double matchBonuses;
		double cupWinnerBonus;
	}
}
