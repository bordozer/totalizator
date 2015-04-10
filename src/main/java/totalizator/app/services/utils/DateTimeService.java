package totalizator.app.services.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeService {

	LocalDateTime getNow();

	LocalDateTime offset( final int measure, final int offset );

	LocalDateTime offset( final LocalDateTime time, final int measure, final int offset );

	String formatDateTime( final LocalDateTime time );

	LocalDateTime parseDate( final String date );
}
