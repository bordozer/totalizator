package totalizator.app.services.remote;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class RemoteContentServiceImpl implements RemoteContentService {

	@Override
	public String getRemoteContent( final String url ) throws IOException {
		return getRemoteContent( new RemoteServerRequest( url ) );
	}

	@Override
	public String getRemoteContent( final RemoteServerRequest request ) throws IOException {

		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( request.getUrl() );
		httpGet.setHeader( "X-Auth-Token", request.getxAuthToken() );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler );
		} catch ( final IOException e ) {
			System.out.println( e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	@Override
	public void storeRemoteContentAsFile( final String url, final File file ) throws IOException {
		final BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( file ) );
		bos.write( getRemoteContent( url ).getBytes( "ISO-8859-1" ) );
		bos.flush();
		bos.close();
	}
}
