package totalizator.app.services;

import totalizator.app.models.Team;

import java.io.File;
import java.io.IOException;

public interface TeamLogoService {

	void deleteLogosDir() throws IOException;

	void createLogosDir() throws IOException;

	void uploadLogo( Team team, File file ) throws IOException;

	File getTeamLogoFile( Team team ) throws IOException;
}
