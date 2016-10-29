package betmen.core.service;

import betmen.core.entity.Cup;

import java.time.LocalDate;
import java.util.List;

public interface CupsAndMatchesService {

    List<Cup> getPublicCupsHaveMatchesOnDate(LocalDate date);
}
