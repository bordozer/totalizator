package totalizator.app.services.points.recalculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.Match;
import totalizator.app.models.MatchPoints;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.points.MatchPointsService;
import totalizator.app.services.points.calculation.match.UserMatchPointsCalculationService;

import java.util.List;
import java.util.function.Consumer;

@Service
public class MatchPointsRecalculationServiceImpl implements MatchPointsRecalculationService {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchPointsService matchPointsService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private UserMatchPointsCalculationService userMatchPointsCalculationService;

	@Override
	public void recalculate( final Match match ) {

		matchPointsService.delete( match );

		if ( ! matchService.isMatchFinished( match ) ) {
			return;
		}

		matchBetsService.getUserWhoMadeBet( match )
				.stream()
				.forEach( new Consumer<User>() {

					@Override
					public void accept( final User user ) {

						saveFor( user, match );

						recalculateForUserGroup( user, userGroupService.loadUserGroupsWhereUserIsOwner( user ), match );

						recalculateForUserGroup( user, userGroupService.loadUserGroupsWhereUserIsMember( user ), match );
					}
				} );
	}

	private void recalculateForUserGroup( final User user, final List<UserGroup> userGroups, final Match match ) {

		userGroups
				.stream()
				.forEach( new Consumer<UserGroup>() {
					@Override
					public void accept( final UserGroup userGroup ) {
						saveFor( user, match, userGroup );
					}
				} );
	}

	private void saveFor( final User user, final Match match ) {

		final UserMatchPointsHolder pointsHolder = userMatchPointsCalculationService.getUserMatchPoints( match, user );

		if ( pointsHolder == null ) {
			return;
		}

		final MatchPoints matchPoints = new MatchPoints();

		matchPoints.setUser( user );
		matchPoints.setMatch( match );
		matchPoints.setCup( match.getCup() );

		matchPoints.setMatchPoints( pointsHolder.getUserMatchBetPointsHolder().getMatchBetPoints() );
		matchPoints.setMatchBonus( pointsHolder.getMatchBonus() );

		matchPoints.setMatchTime( match.getBeginningTime() );

		matchPointsService.save( matchPoints );
	}

	private void saveFor( final User user, final Match match, final UserGroup userGroup ) {

		final MatchPoints matchPoints = new MatchPoints();

		matchPoints.setUser( user );
		matchPoints.setMatch( match );
		matchPoints.setCup( match.getCup() );
		matchPoints.setUserGroup( userGroup );

		final UserMatchPointsHolder pointsHolder = userMatchPointsCalculationService.getUserMatchPoints( match, user, userGroup );

		matchPoints.setMatchPoints( pointsHolder.getUserMatchBetPointsHolder().getMatchBetPoints() );
		matchPoints.setMatchBonus( pointsHolder.getMatchBonus() );

		matchPoints.setMatchTime( match.getBeginningTime() );

		matchPointsService.save( matchPoints );
	}
}
