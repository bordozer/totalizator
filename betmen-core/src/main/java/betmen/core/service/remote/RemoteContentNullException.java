package betmen.core.service.remote;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteContentNullException extends Exception {

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
