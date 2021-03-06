package betmen.core.service.activiries;

import betmen.core.entity.ActivityStreamEntry;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;
import betmen.core.entity.activities.ActivityStreamEntryType;
import betmen.core.entity.activities.MatchActivityStreamEntry;
import betmen.core.entity.activities.MatchBetActivityStreamEntry;
import betmen.core.entity.activities.events.MatchBetEvent;
import betmen.core.entity.activities.events.MatchEvent;
import betmen.core.repository.ActivityStreamDao;
import betmen.core.repository.jpa.ActivityStreamJpaRepository;
import betmen.core.service.utils.DateTimeService;
import betmen.core.service.utils.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityStreamServiceImpl implements ActivityStreamService {

    @Autowired
    private ActivityStreamDao activityStreamRepository;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private ActivityStreamJpaRepository activityStreamJpaRepository;
    @Autowired
    private JsonService jsonService;

    @Override
    public List<AbstractActivityStreamEntry> loadAllForLast(final int hours) {
        return transformEntries(activityStreamRepository.loadAllEarlierThen(dateTimeService.minusHours(hours)));
    }

    @Override
    public List<AbstractActivityStreamEntry> loadAllForMatch(final int matchId) {
        return transformEntries(activityStreamRepository.loadAllForMatch(matchId));
    }

    @Override
    public List<AbstractActivityStreamEntry> loadAllForUser(final int userId, final int qty) {
        return transformEntries(activityStreamRepository.loadAllForUser(userId, qty));
    }

    @Override
    @Transactional
    public void matchBetCreated(final MatchBet matchBet) {
        activityStreamRepository.save(createFromMatchBet(matchBet, ActivityStreamEntryType.MATCH_BET_CREATED, 0, 0));
    }

    @Override
    @Transactional
    public void matchBetChanged(final MatchBet matchBet, final int oldScore1, final int oldScore2) {
        activityStreamRepository.save(createFromMatchBet(matchBet, ActivityStreamEntryType.MATCH_BET_CHANGED, oldScore1, oldScore2));
    }

    @Override
    @Transactional
    public void matchBetDeleted(final User user, final int matchId, final int score1, final int score2) {
        activityStreamRepository.save(getMatchBetEvent(user, matchId, score1, score2, ActivityStreamEntryType.MATCH_BET_DELETED));
    }

    @Override
    @Transactional
    public void matchFinished(final int matchId, final int score1, final int score2) {
        activityStreamRepository.save(getMatchBetEvent(null, matchId, score1, score2, ActivityStreamEntryType.MATCH_FINISHED));
    }

    @Override
    @Transactional
    public void delete(final int id) {
        activityStreamRepository.delete(id);
    }

    @Override
    public void deleteAll() {
        activityStreamJpaRepository.deleteAll();
    }

    private List<AbstractActivityStreamEntry> transformEntries(List<ActivityStreamEntry> activityStreamEntries) {
        return activityStreamEntries
                .stream()
                .map(activityStreamEntry -> {
                    switch (activityStreamEntry.getActivityStreamEntryType()) {
                        case MATCH_BET_CREATED:
                        case MATCH_BET_CHANGED:
                            return new MatchBetActivityStreamEntry(activityStreamEntry, jsonService.fromMatchBetEventJson(activityStreamEntry.getEventJson()));
                        case MATCH_BET_DELETED:
                        case MATCH_FINISHED:
                            return new MatchActivityStreamEntry(activityStreamEntry, jsonService.fromMatchEventJson(activityStreamEntry.getEventJson()));
                    }
                    throw new IllegalArgumentException(String.format("Unsupported activity type: %s", activityStreamEntry.getActivityStreamEntryType()));
                })
                .collect(Collectors.toList());
    }

    private ActivityStreamEntry getMatchBetEvent(final User user, final int matchId, final int score1, final int score2, ActivityStreamEntryType activityStreamEntryType) {
        final ActivityStreamEntry activity = new ActivityStreamEntry(user, dateTimeService.getNow());
        activity.setActivityEntryId(matchId);
        activity.setActivityStreamEntryType(activityStreamEntryType);
        activity.setEventJson(jsonService.toJson(new MatchEvent(matchId, score1, score2)));
        return activity;
    }

    private ActivityStreamEntry createFromMatchBet(final MatchBet matchBet, final ActivityStreamEntryType activityStreamEntryType, final int oldScore1, final int oldScore2) {
        final ActivityStreamEntry activity = new ActivityStreamEntry(matchBet.getUser(), matchBet.getBetTime());
        final int matchId = matchBet.getMatch().getId();
        activity.setActivityEntryId(matchId);
        activity.setActivityStreamEntryType(activityStreamEntryType);
        activity.setEventJson(jsonService.toJson(new MatchBetEvent(matchId, matchBet.getBetScore1(), matchBet.getBetScore2(), oldScore1, oldScore2)));
        return activity;
    }
}
