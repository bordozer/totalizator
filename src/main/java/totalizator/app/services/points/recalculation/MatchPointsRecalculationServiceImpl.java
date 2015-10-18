package totalizator.app.services.points.recalculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.Match;
import totalizator.app.models.MatchPoints;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.points.MatchPointsService;
import totalizator.app.services.points.calculation.match.UserMatchPointsCalculationService;

import java.util.List;
import java.util.function.Consumer;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class MatchPointsRecalculationServiceImpl implements MatchPointsRecalculationService {

	@Autowired
	private MatchPointsService matchPointsService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserMatchPointsCalculationService userMatchPointsCalculationService;

	@Override
	@Transactional
	public void recalculate( final Match match ) {

		collectCalculatedPoints( match )
				.stream()
				.forEach( new Consumer<MatchPoints>() {
					@Override
					public void accept( final MatchPoints matchPoints ) {
						matchPointsService.save( matchPoints );
					}
				} );
	}

	private List<MatchPoints> collectCalculatedPoints( final Match match ) {

		final List<MatchPoints> result = newArrayList();

		matchBetsService.getUserWhoMadeBet( match )
				.stream()
				.forEach( new Consumer<User>() {

					@Override
					public void accept( final User user ) {

						result.add( getFor( user, match ) );

						result.addAll( recalculateForUserGroup( user, userGroupService.loadUserGroupsWhereUserIsOwner( user ), match ) );

						result.addAll( recalculateForUserGroup( user, userGroupService.loadUserGroupsWhereUserIsMember( user ), match ) );
					}
				} );
		return result;
	}

	private List<MatchPoints> recalculateForUserGroup( final User user, final List<UserGroup> userGroups, final Match match ) {

		final List<MatchPoints> result = newArrayList();

		userGroups
				.stream()
				.forEach( new Consumer<UserGroup>() {
					@Override
					public void accept( final UserGroup userGroup ) {
						result.add( getFor( user, match, userGroup ) );
					}
				} );

		return result;
	}

	private MatchPoints getFor( final User user, final Match match ) {
		return getFor( user, match, null );
	}

	private MatchPoints getFor( final User user, final Match match, final UserGroup userGroup ) {

		final UserMatchPointsHolder pointsHolder = getPointsHolder( user, match, userGroup );

		if ( pointsHolder == null ) {
			return null;
		}

		final MatchPoints matchPoints = new MatchPoints();

		matchPoints.setUser( user );
		matchPoints.setMatch( match );
		matchPoints.setCup( match.getCup() );

		if ( userGroup != null ) {
			matchPoints.setUserGroup( userGroup );
		}

		matchPoints.setMatchPoints( pointsHolder.getMatchBetPoints() );
		matchPoints.setMatchBonus( pointsHolder.getMatchBonus() );

		matchPoints.setMatchTime( match.getBeginningTime() );

		return matchPoints;
	}

	private UserMatchPointsHolder getPointsHolder( final User user, final Match match, final UserGroup userGroup ) {

		if ( userGroup == null ) {
			return userMatchPointsCalculationService.calculateUserMatchPoints( match, user );
		}

		return userMatchPointsCalculationService.calculateUserMatchPoints( match, user, userGroup );
	}
}
