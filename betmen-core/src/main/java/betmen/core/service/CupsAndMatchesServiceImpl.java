package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.service.matches.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Cup> getPublicCupsHaveMatchesOnDate(final LocalDate date) {
        return matchService.loadAllOnDate(date)
                .stream()
                .filter(match -> cupService.isCupPublic(match.getCup()))
                .map(Match::getCup)
                .distinct()
                .collect(Collectors.toList());
    }
}
