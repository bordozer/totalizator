package totalizator.app.services;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;

import java.io.File;
import java.io.IOException;

@Service
public class LogoServiceImpl implements LogoService {

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
		FileUtils.copyFile( file, getLogoFile( team ) );
	}

	@Override
	public void uploadLogo( final Cup cup, final File file ) throws IOException {
		FileUtils.copyFile( file, getLogoFile( cup ) );
	}

	@Override
	public void deleteLogo( final Team team ) throws IOException {
		final File logoFile = getLogoFile( team );
		if ( logoFile.exists() ) {
			FileUtils.fileDelete( logoFile.getPath() );
		}
	}

	@Override
	public void deleteLogo( final Cup cup ) throws IOException {
		final File logoFile = getLogoFile( cup );
		if ( logoFile.exists() ) {
			FileUtils.fileDelete( logoFile.getPath() );
		}
	}

	@Override
	public File getLogoFile( final Team team ) throws IOException {

		final File categoryLogosDir = getCategoryLogosDir( team.getCategory() );

		assertDirExists( categoryLogosDir );

		final String logoFileName = team.getLogoFileName();

		if ( StringUtils.isNotEmpty( logoFileName ) ) {
			return new File( categoryLogosDir, logoFileName );
		}

		return null;
	}

	@Override
	public String getLogoURL( final Team team ) {
		return String.format( "/resources/teams/%d/logo/", team.getId() );
	}

	@Override
	public String getLogoURL( final Cup cup ) {
		return String.format( "/resources/cups/%d/logo/", cup.getId() );
	}

	@Override
	public File getLogoFile( final Cup cup ) throws IOException {

		final File categoryLogosDir = getCategoryLogosDir( cup.getCategory() );

		assertDirExists( categoryLogosDir );

		final String logoFileName = cup.getLogoFileName();

		if ( StringUtils.isNotEmpty( logoFileName ) ) {
			return new File( categoryLogosDir, logoFileName );
		}

		return null;
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
