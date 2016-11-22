package betmen.core.service.points.calculation.match;

import betmen.core.entity.UserGroupEntity;
import betmen.core.model.points.UserMatchPointsHolder;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.points.calculation.match.bonus.MatchBonusPointsCalculationService;
import betmen.core.service.points.calculation.match.points.UserMatchBetPointsCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMatchPointsCalculationServiceImpl implements UserMatchPointsCalculationService {

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

    @Autowired
    private MatchBonusPointsCalculationService matchBonusPointsCalculationService;

    @Override
    public UserMatchPointsHolder calculateUserMatchPoints(final Match match, final User user) {

        final MatchBet matchBet = matchBetsService.load(user, match);

        if (matchBet != null) {
            return new UserMatchPointsHolder(userMatchBetPointsCalculationService.getUserMatchBetPoints(matchBet), getUserMatchBonus(matchBet));
        }

        return null;
    }

    @Override
    public UserMatchPointsHolder calculateUserMatchPoints(final Match match, final User user, final UserGroupEntity userGroupEntity) {
        final MatchBet userMatchBet = matchBetsService.load(user, match);
        return new UserMatchPointsHolder(userMatchBetPointsCalculationService.getUserMatchBetPoints(userMatchBet), getUserMatchBonus(userMatchBet, userGroupEntity));
    }

    private float getUserMatchBonus(final MatchBet matchBet) {
        return getUserMatchBonus(matchBet, null); // I'm a bad boy
    }

    private float getUserMatchBonus(final MatchBet matchBet, final UserGroupEntity userGroupEntity) {

        if (userMatchBetPointsCalculationService.getUserMatchBetPoints(matchBet).getPoints() < 0) {
            return 0; // user who made the bet is loser - no bonuses
        }

        if (userGroupEntity == null) {
            return matchBonusPointsCalculationService.calculateMatchBonus(matchBet.getMatch());
        }

        return matchBonusPointsCalculationService.calculateMatchBonus(matchBet.getMatch(), userGroupEntity);
    }
}
