package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.services.matches.MatchService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CupsAndMatchesServiceImpl implements CupsAndMatchesService {

    @Autowired
    private MatchService matchService;

    @Autowired
    private CupService cupService;

    @Override
    public List<Cup> getCupsHaveMatchesOnDate(final LocalDate date) {
        return matchService.loadAllOnDate(date)
                .stream()
                .filter(match -> cupService.isCupPublic(match.getCup()))
                .map(Match::getCup)
                .distinct()
                .collect(Collectors.toList());
    }
}
