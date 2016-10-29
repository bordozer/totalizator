package betmen.core.service.points.recalculation;

import betmen.core.entity.Match;
import betmen.core.entity.MatchPoints;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.model.points.UserMatchPointsHolder;
import betmen.core.service.UserGroupService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.points.MatchPointsService;
import betmen.core.service.points.calculation.match.UserMatchPointsCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public void recalculate(final Match match) {
        collectCalculatedPoints(match).stream().forEach(matchPoints -> matchPointsService.save(matchPoints));
    }

    private List<MatchPoints> collectCalculatedPoints(final Match match) {
        final List<MatchPoints> result = newArrayList();
        matchBetsService.getUserWhoMadeBet(match)
                .stream()
                .forEach(user -> {
                    result.add(getFor(user, match));
                    result.addAll(recalculateForUserGroup(user, userGroupService.loadUserGroupsWhereUserIsOwner(user), match));
                    result.addAll(recalculateForUserGroup(user, userGroupService.loadUserGroupsWhereUserIsMember(user), match));
                });
        return result;
    }

    private List<MatchPoints> recalculateForUserGroup(final User user, final List<UserGroup> userGroups, final Match match) {
        return userGroups.stream().map(userGroup -> getFor(user, match, userGroup)).collect(Collectors.toList());
    }

    private MatchPoints getFor(final User user, final Match match) {
        return getFor(user, match, null);
    }

    private MatchPoints getFor(final User user, final Match match, final UserGroup userGroup) {
        final UserMatchPointsHolder pointsHolder = getPointsHolder(user, match, userGroup);
        if (pointsHolder == null) {
            return null;
        }

        final MatchPoints matchPoints = new MatchPoints();
        matchPoints.setUser(user);
        matchPoints.setMatch(match);
        matchPoints.setCup(match.getCup());
        if (userGroup != null) {
            matchPoints.setUserGroup(userGroup);
        }
        matchPoints.setMatchPoints(pointsHolder.getMatchBetPoints());
        matchPoints.setMatchBonus(pointsHolder.getMatchBonus());
        matchPoints.setMatchTime(match.getBeginningTime());

        return matchPoints;
    }

    private UserMatchPointsHolder getPointsHolder(final User user, final Match match, final UserGroup userGroup) {
        if (userGroup == null) {
            return userMatchPointsCalculationService.calculateUserMatchPoints(match, user);
        }
        return userMatchPointsCalculationService.calculateUserMatchPoints(match, user, userGroup);
    }
}
