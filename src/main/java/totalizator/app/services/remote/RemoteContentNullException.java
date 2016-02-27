package totalizator.app.services.remote;

import org.apache.log4j.Logger;

public class RemoteContentNullException extends Exception {

    private static final Logger LOGGER = Logger.getLogger(RemoteContentNullException.class);

    private String url;

    public RemoteContentNullException(final String url) {
        LOGGER.error(url);
        this.url = url;
    }

    @Override
    public String getMessage() {
        return String.format("URL returns null content: %s", url);
    }
}
