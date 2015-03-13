package totalizator.app.services.utils;

import java.util.Date;

public interface DateTimeService {

	Date getNow();

	Date offset( final int measure, final int offset );

	Date offset( final Date time, final int measure, final int offset );
}
