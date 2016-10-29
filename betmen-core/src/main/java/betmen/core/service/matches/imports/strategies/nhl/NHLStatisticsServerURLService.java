package betmen.core.service.matches.imports.strategies.nhl;

import betmen.core.entity.Team;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NHLStatisticsServerURLService {

    public String getURL(final Team team, final LocalDate date) {
        return String.format("http://nhlwc.cdnak.neulion.com/fs1/nhl/league/clubschedule/%s/%s/%s/iphone/clubschedule.json", team.getImportId(), date.getYear(), date.getMonthValue());
    }
}
