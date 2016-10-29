package betmen.core.service.matches.imports.strategies.uefa;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.RemoteGameParsingService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service(value = "uefaGameParsingService")
public class UEFAGameParsingService implements RemoteGameParsingService {

    @Override
    public Set<RemoteGame> loadGamesFromJSON(final Cup cup, final String remoteGamesJSON) {

        final Gson gson = new Gson();

        final LinkedTreeMap uefaGamesJSON = (LinkedTreeMap) gson.fromJson(remoteGamesJSON, Object.class);

        if (uefaGamesJSON == null) {
            return new TreeSet<RemoteGame>();
        }

        final Set<RemoteGame> result = new TreeSet<RemoteGame>();

        final List fixtures = (ArrayList) uefaGamesJSON.get("fixtures");
        for (final Object _fixture : fixtures) {

            final LinkedTreeMap fixture = (LinkedTreeMap) _fixture;

            final LinkedTreeMap links = (LinkedTreeMap) fixture.get("_links");
            final LinkedTreeMap self = (LinkedTreeMap) links.get("self");
            final String href = (String) self.get("href");

            final String[] strings = href.split("/");
            final String remoteGameId = strings[strings.length - 1];

            final RemoteGame remoteGame = new RemoteGame(remoteGameId);

            initRemoteGame(remoteGame, fixture);

            result.add(remoteGame);
        }

        return result;
    }

    @Override
    public void loadGameFromJSON(final RemoteGame remoteGame, final String remoteGameJSON) throws IOException {

        final Gson gson = new Gson();
        final LinkedTreeMap fixture = (LinkedTreeMap) ((LinkedTreeMap) gson.fromJson(remoteGameJSON, Object.class)).get("fixture");

        initRemoteGame(remoteGame, fixture);
    }

    private void initRemoteGame(final RemoteGame remoteGame, final LinkedTreeMap fixture) {

        final String team1Name = (String) fixture.get("homeTeamName");
        final String team2Name = (String) fixture.get("awayTeamName");

        final String date = (String) fixture.get("date"); // 2015-09-12T14:00:00Z // TODO: timezone
        final LocalDateTime beginningTime = LocalDateTime.parse(date.substring(0, date.length() - 1));

        final LinkedTreeMap scores = (LinkedTreeMap) fixture.get("result");
        final Double score1 = (Double) scores.get("goalsHomeTeam");
        final Double score2 = (Double) scores.get("goalsAwayTeam");

        final String status = (String) fixture.get("status");

        remoteGame.setRemoteTeam1Id(team1Name);
        remoteGame.setRemoteTeam1Name(team1Name);

        remoteGame.setRemoteTeam2Id(team2Name);
        remoteGame.setHomeTeamNumber(1);
        remoteGame.setRemoteTeam2Name(team2Name);

        remoteGame.setBeginningTime(beginningTime);

        remoteGame.setScore1(score1 < 0 ? 0 : (int) (double) score1);
        remoteGame.setScore2(score2 < 0 ? 0 : (int) (double) score2);

        remoteGame.setFinished(status.equals("FINISHED"));

        remoteGame.setLoaded(true);
    }
}
