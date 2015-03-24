package totalizator.app.services;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.io.File;
import java.io.IOException;

@Service
public class TeamLogoServiceImpl implements TeamLogoService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public void deleteLogosDir() throws IOException {
		FileUtils.deleteDirectory( systemVarsService.getLogosPath() );
	}

	@Override
	public void createLogosDir() throws IOException {
		FileUtils.mkdir( systemVarsService.getLogosPath() );
	}

	@Override
	public void uploadLogo( final Team team, final File file ) throws IOException {
		FileUtils.copyFile( file, getTeamLogoFile( team ) );
	}

	@Override
	public void deleteLogo( final Team team ) throws IOException {
		final File logoFile = getTeamLogoFile( team );
		if ( logoFile.exists() ) {
			FileUtils.fileDelete( logoFile.getPath() );
		}
	}

	@Override
	public File getTeamLogoFile( final Team team ) throws IOException {

		final File categoryLogosDir = getCategoryLogosDir( team.getCategory() );

		assertDirExists( categoryLogosDir );

		return new File( categoryLogosDir, team.getLogoFileName() );
	}

	@Override
	public String getTeamLogoURL( final Team team ) {
		return String.format( "/resources/team/%d/logo/", team.getId() );
	}

	private File getCategoryLogosDir( final Category category ) {
		return new File( systemVarsService.getLogosPath(), String.valueOf( category.getId() ) );
	}

	private void assertDirExists( final File categoryDir ) throws IOException {
		if ( ! categoryDir.exists() ) {
			FileUtils.mkdir( categoryDir.getCanonicalPath() );
		}
	}
}
