package betmen.core.service.teams;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.service.CupService;
import betmen.core.service.matches.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class TeamsStandoffServiceImpl implements TeamsStandoffService {

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Override
    public Cup getLastStandoffCup(final Team team1, final Team team2) {

        final List<Cup> cups = cupService.loadPublic(team1.getCategory());
        if (cups.size() == 0) {
            return null;
        }

        final List<Match> matches = matchService.loadAll(cups.get(0).getId(), team1.getId(), team2.getId());

        final List<Match> finishedMatches = matches
                .stream()
                .filter(new Predicate<Match>() {

                    @Override
                    public boolean test(final Match match) {
                        return matchService.isMatchFinished(match);
                    }
                }).collect(Collectors.toList());

        if (finishedMatches.size() > 0) {
            return cups.get(0);
        }

        if (cups.size() > 1) {
            return cups.get(1);
        }

        return null;
    }

    @Override
    public List<TeamsCupStandoff> getTeamsStandoffByCups(final Team team1, final Team team2) {

        final List<Match> matches = matchService.loadAll(team1, team2);

        final List<Cup> cups = matches
                .stream()
                .map(Match::getCup)
                .distinct()
                .filter(cup -> cupService.isCupPublic(cup))
                .sorted((o1, o2) -> o2.getCupStartTime().compareTo(o1.getCupStartTime()))
//				.limit(3)
                .collect(Collectors.toList());

        final List<TeamsCupStandoff> result = newArrayList();

        cups.forEach(cup -> {

            final List<Match> cupMatches = matches.stream()
                    .filter(match -> match.getCup().equals(cup)).collect(Collectors.toList());

            result.add(new TeamsCupStandoff(cup, getScore(cupMatches, team1), getScore(cupMatches, team2)));
        });

        return result;
    }

    private int getScore(final List<Match> matches, final Team team) {
        int result = 0;

        for (final Match match : matches) {
            result += match.getScore1() > match.getScore2() && match.getTeam1().equals(team) ? 1 : 0;
            result += match.getScore1() < match.getScore2() && match.getTeam2().equals(team) ? 1 : 0;
        }

        return result;
    }
}
