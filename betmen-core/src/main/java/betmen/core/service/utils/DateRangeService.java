package betmen.core.service.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateRangeService {

    LocalDate getDateFrom(final TimePeriod timePeriod, final LocalDateTime time);

    LocalDate getDateTo(final TimePeriod timePeriod, final LocalDateTime time);
}
