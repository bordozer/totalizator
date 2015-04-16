package totalizator.app.services;

import java.io.File;
import java.io.IOException;

public interface RemoteContentService {

	String getRemoteContent( final String url ) throws IOException;

	void storeRemoteContentAsFile( final String url, final File file ) throws IOException;
}
