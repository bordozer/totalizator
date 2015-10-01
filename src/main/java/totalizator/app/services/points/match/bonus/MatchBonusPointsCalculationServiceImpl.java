package totalizator.app.services.points.match.bonus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.points.match.points.UserMatchBetPointsCalculationService;

import java.util.List;

@Service
public class MatchBonusPointsCalculationServiceImpl implements MatchBonusPointsCalculationService {

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public float calculateMatchBonus( final Match match ) {
		return calculateMatchBonus( match, matchBetsService.getUserWhoMadeBet( match ) );
	}

	@Override
	public float calculateMatchBonus( final Match match, final UserGroup userGroup ) {
		return calculateMatchBonus( match, userGroupService.loadUserGroupMembers( userGroup ) );
	}

	private float calculateMatchBonus( final Match match, final List<User> users ) {

		final List<MatchBet> matchBets = matchBetsService.loadAll( match );

		int winnersCount = 0;
		int losersPointsSum = 0;

		for ( final MatchBet bet : matchBets ) {

			if ( ! users.contains( bet.getUser() ) ) {
				continue;
			}

			final UserMatchBetPointsHolder userBetPoints = userMatchBetPointsCalculationService.getUserMatchBetPoints( bet );

			if ( userBetPoints.getPoints() > 0 ) {
				winnersCount++;
			}

			if ( userBetPoints.getPoints() < 0 ) {
				losersPointsSum += Math.abs( userBetPoints.getPoints() );
			}
		}

		if ( winnersCount == 0 ) {
			return 0; // nobody won
		}

		return ( float ) losersPointsSum / ( float ) winnersCount;
	}
}
