package betmen.core.service.matches.imports.strategies.nba;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.service.matches.MatchService;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.strategies.StatisticsServerService;
import betmen.core.service.remote.RemoteContentNullException;
import betmen.core.service.utils.DateTimeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Service(value = "nbaStatisticsAPILimitedService")
public class NBAStatisticsAPILimitedService implements StatisticsServerService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private NBAStatisticsAPIService nbaStatisticsAPIService;

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public Set<RemoteGame> preloadRemoteGames(final Cup cup, final LocalDate dateFrom, final LocalDate dateTo) throws IOException, RemoteContentNullException {

        LocalDateTime week = LocalDateTime.of(dateTimeService.plusDays(dateTimeService.getToday(), 7), LocalTime.of(0, 0));

        List<Match> notFinishedMatches = matchService.loadAllNotFinished(cup.getId());

        final List<RemoteGame> importedNotFinishedRemoteGames = getAlreadyImportedNotFinishedRemoteGames(cup, notFinishedMatches);

        String lastImportedRemoteGameId = getLastImportedRemoteGameId(cup);
        final int remoteGameIdForNewGames = Integer.parseInt(lastImportedRemoteGameId.substring(5, 10)) + 1;
        LOGGER.debug(String.format("New remote games are going to be imported since #%d", remoteGameIdForNewGames));

        int curr = remoteGameIdForNewGames;

        List<RemoteGame> newRemoteGamesForImport = newArrayList();
        while (true) {
            String remoteGameId = constructRemoteGameId(cup, curr);

            if (curr > 1230) {
                break;
            }

            RemoteGame remoteGame = new RemoteGame(remoteGameId);
            populateRemoteGame(cup, remoteGame);
            if (remoteGame.getBeginningTime().isAfter(week)) {
                break;
            }
            newRemoteGamesForImport.add(remoteGame);
            curr++;
        }

        final Set<RemoteGame> result = new LinkedHashSet<>(importedNotFinishedRemoteGames);
        result.addAll(newRemoteGamesForImport);

        return result;
    }

    private String getLastImportedRemoteGameId(final Cup cup) {
        Match lastImportedMatch = matchService.loadLastImportedMatch(cup.getId());
        if (lastImportedMatch == null) {
            return getFirstCupRemoteGameId(cup);
        }
        return lastImportedMatch.getRemoteGameId();
    }

    private List<RemoteGame> getAlreadyImportedNotFinishedRemoteGames(final Cup cup, final List<Match> notFinishedMatches) {
        List<Match> importedNotFinishedMatches = notFinishedMatches.stream()
                .filter(match -> StringUtils.isNotEmpty(match.getRemoteGameId()))
                .collect(Collectors.toList());

        return importedNotFinishedMatches.stream()
                .map(match -> {
                    final String remoteGameId = match.getRemoteGameId();
                    final RemoteGame remoteGame = new RemoteGame(remoteGameId);
                    try {
                        populateRemoteGame(cup, remoteGame);
                    } catch (IOException | RemoteContentNullException e) {
                        LOGGER.error(String.format("Can not import remote NBA game data: '%s'", remoteGameId), e);
                    }
                    return remoteGame;
                })
                .sorted((o1, o2) -> o2.getRemoteGameId().compareToIgnoreCase(o1.getRemoteGameId()))
                .collect(Collectors.toList());
    }

    private String getFirstCupRemoteGameId(final Cup cup) {
        return constructRemoteGameId(cup, 1);
    }

    private String constructRemoteGameId(final Cup cup, final int i) {
        return String.format("002%s%05d", cup.getCupStartTime().getYear() - 2000, i); // TODO: Won't work in 30th century!
    }

    @Override
    public void populateRemoteGame(final Cup cup, final RemoteGame remoteGame) throws IOException, RemoteContentNullException {
        nbaStatisticsAPIService.populateRemoteGame(cup, remoteGame);
    }
}
