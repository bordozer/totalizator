package totalizator.app.controllers.rest.admin.imports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.services.SystemVarsService;

import java.io.*;

@Service
public class ImportedGamesDataStorageServiceImpl implements ImportedGamesDataStorageService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public String getGameData( final int remoteGameId ) throws IOException {

		final File file = getRemoteGameFile( remoteGameId );

		if ( ! file.exists() || file.isDirectory() ) {
			return null;
		}

		try (BufferedReader br = new BufferedReader( new FileReader( file ) ) ) {

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
	public void store( final int remoteGameId, final String gameJSON ) throws IOException {
		new BufferedWriter( new FileWriter( getGameFilePath( remoteGameId ) ) ).write( gameJSON );
	}

	private File getRemoteGameFile( final int remoteGameId ) {
		return new File( getGameFilePath( remoteGameId ) );
	}

	private String getGameFilePath( final int remoteGameId ) {
		return String.format( "%s/%02d", systemVarsService.getImportedGamesDataStoragePath(), remoteGameId );
	}
}
