package totalizator.app.services.remote;

import org.apache.log4j.Logger;

public class RemoteContentNullException extends Exception {

	private static final Logger LOGGER = Logger.getLogger( RemoteContentNullException.class );

	public RemoteContentNullException( final String url ) {
		LOGGER.error( url );
	}
}
