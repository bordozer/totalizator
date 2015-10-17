package totalizator.app.services;

import totalizator.app.models.Cup;

import java.time.LocalDate;
import java.util.List;

public interface CupsAndMatchesService {

	List<Cup> getCupsHaveMatchesOnDate( LocalDate date );
}
