package totalizator.app.services.utils;

import java.util.Calendar;
import java.util.Date;

public interface DateTimeService {

	Date offset( final int measure, final int offset );

	Date offset( final Date time, final int measure, final int offset );
}
