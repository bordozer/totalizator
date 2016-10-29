package betmen.core.service.matches.imports.strategies.nba;

import betmen.core.entity.Cup;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.RemoteGameParsingService;
import betmen.core.service.utils.DateTimeService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service(value = "nbaGameParsingService")
public class NBAGameParsingService implements RemoteGameParsingService {

    @Autowired
    private DateTimeService dateTimeService;

    @Override
    public Set<RemoteGame> loadGamesFromJSON(final Cup cup, final String remoteGamesJSON) {

        final Set<RemoteGame> result = new TreeSet<RemoteGame>();

        final Gson gson = new Gson();

        final NBAGame nbaGame = gson.fromJson(remoteGamesJSON, NBAGame.class);

        for (final Object rowSet : ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(0).get("rowSet")))) {
            final List list = (List) rowSet;
            final String remoteGameId = (String) list.get(2);
            result.add(new RemoteGame(remoteGameId));
        }

        return result;
    }

    @Override
    public void loadGameFromJSON(final RemoteGame remoteGame, final String remoteGameJSON) {

        final Gson gson = new Gson();

        final NBAGame nbaGame = gson.fromJson(remoteGameJSON, NBAGame.class);

        final LocalDateTime gameTime = getDate(nbaGame);

        final Double score1 = (Double) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(0)).get(21);
        final Double score2 = (Double) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(1)).get(21);

        final boolean isFinal = ((String) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(0).get("rowSet")).get(0)).get(4)).equals("Final");

        final String team1Id = new BigDecimal((Double) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(0)).get(3)).toString();
        final String team2Id = new BigDecimal((Double) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(1)).get(3)).toString();
        final String homeTeamId = new BigDecimal((Double) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(0).get("rowSet")).get(0)).get(6)).toString();

        final String _team1 = (String) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(0)).get(5);

        final String _team2 = (String) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(1).get("rowSet")).get(1)).get(5);

        final int homeTeamNumber = homeTeamId.equals(team1Id) ? 1 : 2;

        remoteGame.setBeginningTime(gameTime);

        remoteGame.setRemoteTeam1Id(team1Id);
        remoteGame.setRemoteTeam1Name(_team1);
        if (score1 != null) {
            remoteGame.setScore1(score1.intValue());
        }

        remoteGame.setRemoteTeam2Id(team2Id);
        remoteGame.setRemoteTeam2Name(_team2);
        if (score2 != null) {
            remoteGame.setScore2(score2.intValue());
        }

        remoteGame.setFinished(isFinal);
        remoteGame.setHomeTeamNumber(homeTeamNumber);
    }

    private LocalDateTime getDate(final NBAGame nbaGame) {
        // 2015-04-15T00:00:00
        final String _date = (String) ((ArrayList) ((ArrayList) nbaGame.getResultSets().get(0).get("rowSet")).get(0)).get(0);

        final LocalDateTime time = LocalDateTime.parse(_date);

        return dateTimeService.plusHours(time, 15);
    }

    public void setDateTimeService(final DateTimeService dateTimeService) {
        this.dateTimeService = dateTimeService;
    }
}
