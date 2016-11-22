package betmen.web.converters;

import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;
import betmen.core.entity.activities.ActivityStreamEntryType;
import betmen.core.entity.activities.MatchActivityStreamEntry;
import betmen.core.entity.activities.MatchBetActivityStreamEntry;
import betmen.core.entity.activities.events.MatchBetEvent;
import betmen.core.entity.activities.events.MatchEvent;
import betmen.core.model.points.UserMatchBetPointsHolder;
import betmen.core.service.activiries.ActivityStreamValidator;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.points.calculation.match.bonus.MatchBonusPointsCalculationService;
import betmen.core.service.points.calculation.match.points.UserMatchBetPointsCalculationService;
import betmen.core.utils.Constants;
import betmen.dto.dto.ActivityStreamDTO;
import betmen.dto.dto.UsersRatingPositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ActivityDTOServiceImpl implements ActivityDTOService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private DTOService dtoService;

    @Autowired
    private ActivityStreamValidator activityStreamValidator;

    @Autowired
    private UserMatchBetPointsCalculationService userMatchBetPointsCalculationService;

    @Autowired
    private MatchBonusPointsCalculationService matchBonusPointsCalculationService;

    @Override
    @Cacheable(value = Constants.ACTIVITY_DTO_SERVICE_CACHE_ACTIVITY)
    public ActivityStreamDTO transformActivity(final AbstractActivityStreamEntry activity, final User currentUser) {

        if (!activityStreamValidator.validate(activity)) {
            return null;
        }

        final ActivityStreamDTO dto = new ActivityStreamDTO();

        dto.setActivityStreamEntryTypeId(activity.getActivityStreamEntryType().getId());

        if (activity.getActivityOfUser() != null) {
            dto.setActivityOfUser(dtoService.transformUser(activity.getActivityOfUser()));
        }
        dto.setActivityTime(activity.getActivityTime());

        initActivitySpecific(activity, currentUser, dto);

        return dto;
    }

    private void initActivitySpecific(final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto) {

        switch (activity.getActivityStreamEntryType()) {
            case MATCH_BET_CREATED:
            case MATCH_BET_CHANGED:
                initMatchBet(activity, currentUser, dto);
                return;
            case MATCH_BET_DELETED:
            case MATCH_FINISHED:
                initMatch(activity, currentUser, dto);
                return;
        }
    }

    private void initMatch(final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto) {

        final MatchActivityStreamEntry matchBetActivity = (MatchActivityStreamEntry) activity;

        final Match match = matchService.load(matchBetActivity.getActivityEntryId());

        if (match == null) {
            return;
        }

        dto.setMatch(dtoService.transformMatch(match, currentUser));

        final boolean showBetData = matchBetActivity.getActivityStreamEntryType() != ActivityStreamEntryType.MATCH_FINISHED && showBetData(currentUser, matchBetActivity, match);
        dto.setShowBetData(showBetData);

        if (showBetData) {

            final MatchEvent event = matchBetActivity.getMatchEvent();

            dto.setScore1(event.getScore1());
            dto.setScore2(event.getScore2());

            dto.setShowOldScores(false);
        }
    }

    private void initMatchBet(final AbstractActivityStreamEntry activity, final User currentUser, final ActivityStreamDTO dto) {

        final MatchBetActivityStreamEntry matchBetActivity = (MatchBetActivityStreamEntry) activity;

        final Match match = matchService.load(matchBetActivity.getActivityEntryId());
        dto.setMatch(dtoService.transformMatch(match, currentUser));

        final boolean showBetData = showBetData(currentUser, matchBetActivity, match);
        dto.setShowBetData(showBetData);

        if (showBetData) {

            final MatchBetEvent event = matchBetActivity.getMatchBetEvent();

            dto.setScore1(event.getScore1());
            dto.setScore2(event.getScore2());

            dto.setOldScore1(event.getOldScore1());
            dto.setOldScore2(event.getOldScore2());

            dto.setShowOldScores(matchBetActivity.getActivityStreamEntryType() == ActivityStreamEntryType.MATCH_BET_CHANGED);

            if (matchService.isMatchFinished(match)) {
                User activityOfUser = matchBetActivity.getActivityOfUser();

                MatchBet activityMatchBet = new MatchBet();
                activityMatchBet.setMatch(match);
                activityMatchBet.setUser(activityOfUser);
                activityMatchBet.setBetScore1(matchBetActivity.getMatchBetEvent().getScore1());
                activityMatchBet.setBetScore2(matchBetActivity.getMatchBetEvent().getScore2());

                UserMatchBetPointsHolder userMatchBetPoints = userMatchBetPointsCalculationService.getUserMatchBetPoints(activityMatchBet);
                int matchBetPoints = userMatchBetPoints.getMatchBetPoints();
                float matchBonus = matchBetPoints > 0 ? matchBonusPointsCalculationService.calculateMatchBonus(match) : 0;

                dto.setActivityBetPoints(new UsersRatingPositionDTO(dtoService.transformUser(activityOfUser), matchBetPoints, matchBonus));
            }
        }
    }

    private boolean showBetData(final User currentUser, final AbstractActivityStreamEntry matchBetActivity, final Match match) {
        return matchBetActivity.getActivityOfUser().equals(currentUser) || matchBetsService.userCanSeeAnotherBets(match, currentUser);
    }

}
