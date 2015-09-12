package totalizator.app.services.remote;

import java.io.File;
import java.io.IOException;

public interface RemoteContentService {

	String getRemoteContent( final String url ) throws IOException;

	String getRemoteContent( final RemoteServerRequest request ) throws IOException;

	void storeRemoteContentAsFile( final String url, final File file ) throws IOException;
}
