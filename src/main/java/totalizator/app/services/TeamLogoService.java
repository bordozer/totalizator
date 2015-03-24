package totalizator.app.services;

import totalizator.app.models.Team;

import java.io.File;
import java.io.IOException;

public interface TeamLogoService {

	void deleteLogosDir() throws IOException;

	void createLogosDir() throws IOException;

	void uploadLogo( final Team team, final File file ) throws IOException;

	void deleteLogo( final Team team ) throws IOException;

	File getTeamLogoFile( final Team team ) throws IOException;

	String getTeamLogoURL( final Team team );
}
