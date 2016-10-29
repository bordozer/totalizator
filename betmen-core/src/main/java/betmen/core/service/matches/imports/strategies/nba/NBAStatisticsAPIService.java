package betmen.core.service.matches.imports.strategies.nba;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.ImportedGamesDataStorageService;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.RemoteGameParsingService;
import betmen.core.service.matches.imports.StatisticsServerURLService;
import betmen.core.service.matches.imports.strategies.StatisticsServerService;
import betmen.core.service.remote.RemoteContentNullException;
import betmen.core.service.remote.RemoteContentService;
import betmen.core.service.utils.DateTimeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Service
public class NBAStatisticsAPIService implements StatisticsServerService {

    public static final String NBA = "NBA";

	/*
        More about NBS API: https://github.com/ins429/ins
	*/

    @Autowired
    private RemoteContentService remoteContentService;

    @Autowired
    private RemoteGameParsingService nbaGameParsingService;

    @Autowired
    private ImportedGamesDataStorageService importedGamesDataStorageService;

    @Autowired
    private StatisticsServerURLService nbaStatisticsServerURLService;

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public Set<RemoteGame> preloadRemoteGames(final Cup cup, final LocalDate dateFrom, final LocalDate dateTo) throws IOException, RemoteContentNullException {

        final Set<RemoteGame> result = new TreeSet<RemoteGame>();

        LocalDate date = dateFrom;
        while (true) {

            result.addAll(nbaGameParsingService.loadGamesFromJSON(cup, remoteContentService.getRemoteContent(nbaStatisticsServerURLService.remoteGamesIdsURL(cup, date))));

            date = dateTimeService.plusDays(date, 1);
            if (date.isAfter(dateTo)) {
                break;
            }
        }

        return result;
    }

    @Override
    public void populateRemoteGame(Cup cup, final RemoteGame remoteGame) throws IOException, RemoteContentNullException {

        final String remoteGameJSON = getRemoteGameJSON(cup, remoteGame.getRemoteGameId());

        if (StringUtils.isEmpty(remoteGameJSON)) {
            return;
        }

        nbaGameParsingService.loadGameFromJSON(remoteGame, remoteGameJSON);

        if (remoteGame.isFinished()) {
            importedGamesDataStorageService.store(NBA, remoteGame.getRemoteGameId(), remoteGameJSON);
        }

        remoteGame.setLoaded(true);
    }

    private String getRemoteGameJSON(final Cup cup, final String remoteGameId) throws IOException, RemoteContentNullException {

        final String gameJSON = importedGamesDataStorageService.getGameData(NBA, remoteGameId);
        if (StringUtils.isNotEmpty(gameJSON)) {
            return gameJSON;
        }

        return remoteContentService.getRemoteContent(nbaStatisticsServerURLService.loadRemoteGameURL(cup, remoteGameId));
    }
}
