package betmen.core.service.remote;

public class RemoteServerRequest {

    private final String url;
    private String xAuthToken;

    public RemoteServerRequest(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getxAuthToken() {
        return xAuthToken;
    }

    public void setxAuthToken(final String xAuthToken) {
        this.xAuthToken = xAuthToken;
    }
}
