package betmen.core.service.matches;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.model.ErrorCodes;
import betmen.core.model.ValidationResult;
import betmen.core.repository.MatchBetDao;
import betmen.core.repository.jpa.MatchBetJpaRepository;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.UserGroupService;
import betmen.core.service.activiries.ActivityStreamService;
import betmen.core.service.utils.DateTimeService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

    @Autowired
    private MatchBetDao matchBetRepository;

    @Autowired
    private MatchBetJpaRepository matchBetJpaRepository;

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private CupBetsService cupBetsService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ActivityStreamService activityStreamService;

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll() {
        return Lists.newArrayList(matchBetRepository.loadAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll(final User user) {
        return Lists.newArrayList(matchBetRepository.loadAll(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll(final User user, final LocalDate date) {
        return matchBetRepository.loadAll(user, dateTimeService.getFirstSecondOf(date), dateTimeService.getLastSecondOf(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll(final Match match) {
        return Lists.newArrayList(matchBetRepository.loadAll(match));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll(final Match match, final UserGroupEntity userGroupEntity) {
        return loadAll(match).stream().filter(matchBet -> userGroupService.isUserMemberOfGroup(userGroupEntity, matchBet.getUser())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchBet> loadAll(final Cup cup, final User user) {
        return loadAll(user).stream().filter(matchBet -> matchBet.getMatch().getCup().equals(cup)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MatchBet load(final User user, final Match match) {
        return matchBetRepository.load(user, match);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchBet load(final int id) {
        return matchBetRepository.load(id);
    }

    @Override
    @Transactional
    public MatchBet save(final MatchBet entry) {
        final int entryId = entry.getId();
        final MatchBet savedBet = matchBetRepository.load(entryId);
        final MatchBet matchBet = matchBetRepository.save(entry);
        if (entryId == 0) {
            activityStreamService.matchBetCreated(matchBet);
        } else {
            activityStreamService.matchBetChanged(matchBet, savedBet.getBetScore1(), savedBet.getBetScore2());
        }
        return matchBet;
    }

    @Override
    @Transactional
    public void delete(final int id) {
        matchBetRepository.delete(id);
    }

    @Override
    @Transactional
    public boolean delete(final int userId, final int matchBetId) {
        final MatchBet matchBet = load(matchBetId);
        if (matchBet.getUser().getId() != userId) {
            return false;
        }
        boolean deleted = matchBetJpaRepository.deleteByUserIdAndId(userId, matchBetId) > 0;
        if (deleted) {
            final Match match = matchBet.getMatch();
            final User user = matchBet.getUser();
            activityStreamService.matchBetDeleted(user, match.getId(), matchBet.getBetScore1(), matchBet.getBetScore2());
        }
        return deleted;
    }

    @Override
    public int betsCount(final int matchId) {
        return matchBetRepository.betsCount(matchId);
    }

    @Override
    public int betsCount(final Cup cup, final User user) {
        return matchBetRepository.betsCount(cup, user);
    }

    @Override
    public int userBetsCount(final User user) {
        return matchBetRepository.userBetsCount(user.getId());
    }

    @Override
    public ValidationResult validateBettingAllowed(final Match match, final User user) {
        final Language language = translatorService.getDefaultLanguage(); // TODO: read user language
        final Cup cup = match.getCup();
        if (cupService.isCupFinished(cup)) {
            return ValidationResult.fail(ErrorCodes.CUP_FINISHED, translatorService.translate("Cup $1 is finished", language, cup.getCupName()));
        }
        if (cupBetsService.isMatchBettingFinished(cup)) {
            return ValidationResult.fail(ErrorCodes.MATCH_BETTING_FINISHED, translatorService.translate("Cup $1 is not open for game bets at this moment", language, cup.getCupName()));
        }
        if (matchService.isMatchFinished(match)) {
            return ValidationResult.fail(ErrorCodes.MATCH_FINISHED, translatorService.translate("Match finished", language));
        }
        if (matchService.isMatchStarted(match)) {
            return ValidationResult.fail(ErrorCodes.MATCH_HAS_ALREADY_STARTED, translatorService.translate("Match betting is not allowed after match start ( $1 )", language, dateTimeService.formatDateTimeUI(match.getBeginningTime())));
        }
        return ValidationResult.pass();
    }

    @Override
    public boolean canMatchBeBet(final Match match, final User user) {
        return validateBettingAllowed(match, user).isPassed();
    }

    @Override
    public boolean userCanSeeAnotherBets(final Match match, final User accessor) {
        return matchService.isMatchStarted(match) || matchService.isMatchFinished(match); // TODO: yes, looks weird, I know
    }

    @Override
    public boolean isAllowedToShowMatchBets(final MatchBet matchBet, final User user) {
        if (matchBet.getUser().equals(user)) {
            return true;
        }
        return userCanSeeAnotherBets(matchBet.getMatch(), user);
    }

    @Override
    public List<User> getUserWhoMadeBet(final Match match) {
        return loadAll(match)
                .stream()
                .map(MatchBet::getUser)
                .collect(Collectors.toList());
    }

    @Override
    public int getMatchesCountAccessibleBorBetting(final Cup cup, final User user) {
        return matchBetRepository.getMatchesCountAccessibleForBettingSince(cup, user, dateTimeService.getNow());
    }

    @Override
    public Match getFirstMatchWithoutBet(final Cup cup, final User user) {
        return matchBetRepository.getFirstMatchWithoutBetSince(cup, user, dateTimeService.getNow());
    }

    @Override
    public LocalDateTime getFirstMatchWithoutBetTime(final Cup cup, final User user) {
        final Match match = getFirstMatchWithoutBet(cup, user);
        return match != null ? match.getBeginningTime() : null;
    }
}
