package betmen.core.service.points.calculation.match.bonus;

import betmen.core.entity.UserGroupEntity;
import betmen.core.model.points.UserMatchBetPointsHolder;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.service.UserGroupService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.points.calculation.match.points.UserMatchBetPointsCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public float calculateMatchBonus(final Match match) {
        return calculateMatchBonus(match, matchBetsService.getUserWhoMadeBet(match));
    }

    @Override
    public float calculateMatchBonus(final Match match, final UserGroupEntity userGroupEntity) {
        return calculateMatchBonus(match, userGroupService.loadUserGroupMembers(userGroupEntity));
    }

    private float calculateMatchBonus(final Match match, final List<User> users) {

        final List<MatchBet> matchBets = matchBetsService.loadAll(match);

        int winnersCount = 0;
        int losersPointsSum = 0;

        for (final MatchBet bet : matchBets) {

            if (!users.contains(bet.getUser())) {
                continue;
            }

            final UserMatchBetPointsHolder userBetPoints = userMatchBetPointsCalculationService.getUserMatchBetPoints(bet);

            if (userBetPoints.getPoints() > 0) {
                winnersCount++;
            }

            if (userBetPoints.getPoints() < 0) {
                losersPointsSum += Math.abs(userBetPoints.getPoints());
            }
        }

        if (winnersCount == 0) {
            return 0; // nobody won
        }

        return (float) losersPointsSum / (float) winnersCount;
    }
}
