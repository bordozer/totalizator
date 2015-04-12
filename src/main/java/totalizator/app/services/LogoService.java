package totalizator.app.services;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;

import java.io.File;
import java.io.IOException;

public interface LogoService {

	void deleteLogosDir() throws IOException;

	void createLogosDir() throws IOException;

	void uploadLogo( final Team team, final File file ) throws IOException;

	void uploadLogo( final Cup cup, final File file ) throws IOException;

	void deleteLogo( final Team team ) throws IOException;

	void deleteLogo( final Cup cup ) throws IOException;

	File getLogoFile( final Team team ) throws IOException;

	File getLogoFile( final Cup cup ) throws IOException;

	String getLogoURL( final Team team );

	String getLogoURL( final Cup cup );
}
