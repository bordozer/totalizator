package totalizator.app.controllers.rest.admin.imports;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.services.SystemVarsService;

import java.io.*;

@Service
public class ImportedGamesDataStorageServiceImpl implements ImportedGamesDataStorageService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public String getGameData( final String remoteGameId ) throws IOException {

		final File file = getRemoteGameFile( remoteGameId );

		if ( !file.exists() || file.isDirectory() ) {
			return null;
		}

		try (BufferedReader br = new BufferedReader( new FileReader( file ) )) {

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while ( line != null ) {
				sb.append( line );
				sb.append( System.lineSeparator() );
				line = br.readLine();
			}
			return sb.toString();
		}
	}

	@Override
	public void store( final String remoteGameId, final String gameJSON ) throws IOException {

		final File gameFile = getRemoteGameFile( remoteGameId );
		if ( gameFile.exists() ) {
			FileUtils.deleteQuietly( gameFile );
		}

		final PrintWriter writer = new PrintWriter( getGameFilePath( remoteGameId ), "UTF-8" );
		writer.println( gameJSON );
		writer.close();
	}

	private File getRemoteGameFile( final String remoteGameId ) {
		return new File( getGameFilePath( remoteGameId ) );
	}

	private String getGameFilePath( final String remoteGameId ) {
		return String.format( "%s/%s", systemVarsService.getImportedGamesDataStoragePath(), remoteGameId );
	}
}
