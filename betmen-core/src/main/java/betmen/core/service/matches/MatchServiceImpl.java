package betmen.core.service.matches;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.Team;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.MatchDao;
import betmen.core.repository.jpa.MatchJpaRepository;
import betmen.core.service.points.MatchPointsService;
import betmen.core.service.utils.DateTimeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchDao matchRepository;

    @Autowired
    private MatchJpaRepository matchJpaRepository;

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private MatchPointsService matchPointsService;

    @Autowired
    private DateTimeService dateTimeService;

    private static final Logger LOGGER = Logger.getLogger(MatchServiceImpl.class);

    @Override
    public Match loadAndAssertExists(final int matchId) {
        Match category = load(matchId);
        if (category == null) {
            LOGGER.warn(String.format("Cannot get match with ID: %d", matchId));
            throw new UnprocessableEntityException("Match does not exist");
        }
        return category;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> loadAll() {
        return matchRepository.loadAll().stream()/*.sorted(MATCH_BEGINNING_TIME_DESC_COMPARATOR)*/.collect(Collectors.toList());
    }

    @Override
    public List<Match> loadAll(final Cup cup) {
        return matchRepository.loadAll(cup).stream()/*.sorted(MATCH_BEGINNING_TIME_DESC_COMPARATOR)*/.collect(Collectors.toList());
    }

    @Override
    public List<Match> loadAllNotFinished(final int cupId) {
        return matchRepository.loadAllNotFinished(cupId);
    }

    @Override
    public List<Match> loadAllFinished(final int cupId) {
        return matchRepository.loadAllFinished(cupId);
    }

    @Override
    public List<Match> loadAll(final int cupId, final int teamId) {
        return matchRepository.loadAll(cupId, teamId).stream()/*.sorted(MATCH_BEGINNING_TIME_DESC_COMPARATOR)*/.collect(Collectors.toList());
    }

    @Override
    public List<Match> loadAllFinished(final int cupId, final int teamId) {
        return matchRepository.loadAll(cupId, teamId).stream()
                .filter(this::isMatchFinished)
//                .sorted(MATCH_BEGINNING_TIME_DESC_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> loadAll(final Team team) {
        return matchRepository.loadAll(team);
    }

    @Override
    @Transactional
    public Match save(final Match match) {
        return matchRepository.save(match);
    }

    @Override
    @Transactional(readOnly = true)
    public Match load(final int matchId) {
        return matchRepository.load(matchId);
    }

    @Override
    @Transactional
    public void delete(final int matchId) {
        final List<MatchBet> bets = matchBetsService.loadAll(load(matchId));
        for (final MatchBet bet : bets) {
            matchBetsService.delete(bet.getId());
        }
        matchPointsService.delete(load(matchId));
        // TODO: delete activities
        matchRepository.delete(matchId);
    }

    @Override
    public boolean isMatchStarted(final Match match) {
        return dateTimeService.getNow().isAfter(match.getBeginningTime());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isMatchFinished(final Match match) {
        return match.isMatchFinished();
    }

    @Override
    public Match find(final Cup cup, final Team team1, final Team team2, final LocalDateTime localDateTime) {
        return loadAll(cup.getId(), team1.getId(), team2.getId()).stream()
                .filter(match -> match.getBeginningTime().equals(localDateTime))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Match> loadAll(final Team team1, final Team team2) {
        return newArrayList(matchRepository.loadAll(team1, team2));
    }

    @Override
    public List<Match> loadAll(final int cupId, final int team1Id, final int team2Id) {
        return newArrayList(matchRepository.loadAll(cupId, team1Id, team2Id));
    }

    @Override
    public List<Match> loadAllFinished(final int cupId, final int team1Id, final int team2Id) {
        return newArrayList(matchRepository.loadAllFinished(cupId, team1Id, team2Id));
    }

    @Override
    public boolean isWinner(final Match match, final Team team) {
        if (match.getTeam1().equals(team)) {
            return match.getScore1() > match.getScore2();
        }
        return match.getScore1() < match.getScore2();
    }

    @Override
    public int getMatchCount(final int cupId) {
        return matchJpaRepository.matchesCount(cupId);
    }

    @Override
    public int getMatchCount(final Cup cup, final Team team) {
        return matchRepository.getMatchCount(cup, team);
    }

    @Override
    public int getFinishedMatchCount(final Cup cup, final Team team) {
        return matchRepository.getFinishedMatchCount(cup, team);
    }

    @Override
    public int getWonMatchCount(final Cup cup, final Team team) {
        int result = 0;
        final List<Match> matches = loadAllFinished(cup.getId(), team.getId());
        for (final Match match : matches) {
            if (isWinner(match, team)) {
                result += 1;
            }
        }
        return result;
    }

    @Override
    public int getFutureMatchCount(final Cup cup) {
        return matchRepository.getFutureMatchCount(cup);
    }

    @Override
    public int getFutureMatchCount(Cup cup, Team team) {
        return matchRepository.getFutureMatchCount(cup, team);
    }

    @Override
    public Match findByImportId(final String remoteGameId) {
        return matchRepository.findByImportId(remoteGameId);
    }

    @Override
    public List<Match> loadAllBetween(final LocalDateTime timeFrom, final LocalDateTime timeTo) {
        return matchRepository.loadAllBetween(timeFrom, timeTo);
    }

    @Override
    public List<Match> loadAllBetween(final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo) {
        return matchRepository.loadAllBetween(cupId, timeFrom, timeTo);
    }

    @Override
    public List<Match> loadAllOnDate(final LocalDate date) {
        return loadAllBetween(dateTimeService.getFirstSecondOf(date), dateTimeService.getLastSecondOf(date));
    }

    @Override
    public List<Match> loadAllOnDate(final int cupId, final LocalDate date) {
        return loadAllBetween(cupId, dateTimeService.getFirstSecondOf(date), dateTimeService.getLastSecondOf(date));
    }

    @Override
    public List<Match> getMatchNotFinishedYetMatches(final Cup cup) {
        return matchRepository.getStartedMatchCount(cup, dateTimeService.getNow());
    }

    @Override
    public Match getNearestFutureMatch(final Cup cup, final LocalDateTime onTime) {
        return matchRepository.getNearestFutureMatch(cup, onTime);
    }

    @Override
    public Match getFirstMatch(final Cup cup) {
        return matchRepository.getFirstMatch(cup);
    }

    @Override
    public List<Match> getLastNMatches(final Cup cup, final Team team, final int n) {
        return matchRepository.getLastNMatches(cup, team, n);
    }

    @Override
    public Match loadLastImportedMatch(final int cupId) {
        return matchRepository.loadLastImportedMatch(cupId);
    }
}
