package totalizator.app.controllers.ui.resources;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.services.CategoryService;
import totalizator.app.services.CupService;
import totalizator.app.services.LogoService;
import totalizator.app.services.TeamService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping( "/resources" )
public class ResourcesController {

	private static final String CONTENT_TYPE = "image/jpeg";
	private static final File IMAGE_NOT_FOUND_FILE = new File( "src/main/webapp/resources/img/team-logo-not-found.png" );

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private LogoService logoService;

	@RequestMapping( "categories/{categoryId}/logo/" )
	public void categoryLogo( final @PathVariable( "categoryId" ) int categoryId, final HttpServletResponse response ) throws IOException {
		downloadFile( logoService.getLogoFile( categoryService.load( categoryId ) ), response );
	}

	@RequestMapping( "cups/{cupId}/logo/" )
	public void cupLogo( final @PathVariable( "cupId" ) int cupId, final HttpServletResponse response ) throws IOException {

		final Cup cup = cupService.load( cupId );

		final File cupLogo = logoService.getLogoFile( cup );
		if ( isLogo( cupLogo ) ) {
			downloadFile( cupLogo, response );
			return;
		}

		downloadFile( logoService.getLogoFile( cup.getCategory() ), response );
	}

	@RequestMapping( "teams/{teamId}/logo/" )
	public void teamLogo( final @PathVariable( "teamId" ) int teamId, final HttpServletResponse response ) throws IOException {
		downloadFile( logoService.getLogoFile( teamService.load( teamId ) ), response );
	}

	private void downloadFile( final File beingDownloadedFile, final HttpServletResponse response ) throws IOException {

		final File file = isLogo( beingDownloadedFile ) ? beingDownloadedFile : IMAGE_NOT_FOUND_FILE;

		response.setContentLength( ( int ) file.length() );
		response.setContentType( CONTENT_TYPE );

		final OutputStream outputStream = response.getOutputStream();

		response.setHeader( "Content-Disposition", contentDisposition( file ) );

		pipe( file, outputStream );
		outputStream.close();
	}

	private boolean isLogo( final File beingDownloadedFile ) {
		return beingDownloadedFile != null && beingDownloadedFile.isFile() && beingDownloadedFile.exists();
	}

	private static String contentDisposition( final File file ) {
		return String.format( "filename=\"%s\"", file.getName() );
	}

	private static int pipe( final File file, final OutputStream out ) throws IOException {
		final FileInputStream fis = new FileInputStream( file );
		int total = pipe( fis, out );
		fis.close();
		return total;
	}

	private static int pipe( final InputStream inputStream, final OutputStream outputStream ) throws IOException {
		final long copied = IOUtils.copyLarge( inputStream, outputStream );
		if ( copied >= 0 ) {
			return 1;
		}
		return -1;
	}
}
