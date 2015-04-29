package totalizator.app.controllers.rest.admin.imports;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Cup;
import totalizator.app.services.SystemVarsService;

import java.io.*;

@Service
public class ImportedGamesDataStorageServiceImpl implements ImportedGamesDataStorageService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public String getGameData( final Cup cup, final String remoteGameId ) throws IOException {

		final File file = getRemoteGameFile( cup, remoteGameId );

		if ( !file.exists() || file.isDirectory() ) {
			return null;
		}

		try ( final BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {

			final StringBuilder sb = new StringBuilder();
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
	public void store( final Cup cup, final String remoteGameId, final String gameJSON ) throws IOException {

		final File gameFile = getRemoteGameFile( cup, remoteGameId );

		final File yearFolder = gameFile.getParentFile();
		if ( ! yearFolder.exists() ) {
			FileUtils.forceMkdir( yearFolder );
		}

		if ( gameFile.exists() ) {
			FileUtils.deleteQuietly( gameFile );
		}

		final PrintWriter writer = new PrintWriter( gameFile, "UTF-8" );
		writer.println( gameJSON );
		writer.close();
	}

	private File getRemoteGameFile( final Cup cup, final String remoteGameId ) {
		return new File( getGameFilePath( cup, remoteGameId ) );
	}

	private String getGameFilePath( final Cup cup, final String remoteGameId ) {
		final int year = cup.getCupStartTime().getYear();
		return String.format( "%s/%d/%s", systemVarsService.getImportedGamesDataStorageDir(), year, remoteGameId );
	}
}
