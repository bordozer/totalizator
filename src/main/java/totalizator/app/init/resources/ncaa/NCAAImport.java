package totalizator.app.init.resources.ncaa;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import totalizator.app.init.initializers.TeamImportService;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class NCAAImport {

	public static void main( String[] args ) throws IOException {
		final NCAAImport importer = new NCAAImport();
		importer.makeImport();
	}

	private void makeImport() throws IOException {

		final String content = getRemotePageContent( "http://www.foxsports.com/college-basketball/teams" );

		final List<NCAATeam> teams = getTeams( content );

		final Document document = DocumentHelper.createDocument();
		final Element rootElement = document.addElement( "teams" );

		for ( final NCAATeam team : teams ) {

			final Element teamElement = rootElement.addElement( "team" );

			final String teamName = generateTeamName( team );
			final String logoName = generateLogoName( team );

			teamElement.addElement( "name" ).addText( teamName );
			teamElement.addElement( "logo" ).addText( logoName );

			final String imageContent = getImageContentFromUrl( team.logoURL );

			final File image = new File( TeamImportService.RESOURCES_DIR + "/ncaa/logos/" + logoName );
			writeImageContentToFile( image, imageContent );
		}

		final FileWriter fileWriter = new FileWriter( new File( TeamImportService.RESOURCES_DIR + "/ncaa/teams.xml" ) );
		final OutputFormat format = OutputFormat.createPrettyPrint();
		final XMLWriter output = new XMLWriter( fileWriter, format );
		output.write( document );
		output.close();
	}

	private  static void writeImageContentToFile( final File imageFile, final String imageContent ) throws IOException {
		final BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( imageFile ) );
		bos.write( imageContent.getBytes( "ISO-8859-1" ) );
		bos.flush();
		bos.close();
	}

	private String generateTeamName( final NCAATeam team ) {
		return String.format( String.format( "%s %s", team.teamState, team.teamName ).trim() );
	}

	private String generateLogoName( final NCAATeam team ) {
		return String.format( String.format( "%s %s.jpg", team.teamState, team.teamName ).trim() ).replace( " ", "_" ).replace( "'", "" );
	}

	public String getImageContentFromUrl( final String imageUrl ) {

		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( imageUrl );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler ); // -XX:-LoopUnswitching
		} catch ( final IOException e ) {
			System.out.println( e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	private List<NCAATeam> getTeams( final String content ) {
		final List<NCAATeam> result = newArrayList();

		final Pattern pattern = Pattern.compile( "(?s)<div class=\"wisfb_fullTeamStacked\">(.*?)</div>" );
		final Matcher matcher = pattern.matcher( content );
		while ( matcher.find() ) {
			final String teamSection = matcher.group( 1 );
			final String logoURL = getLogo( teamSection );
			final String teamState = getTeamState( teamSection );
			final String teamName = getTeamName( teamSection );

			result.add( new NCAATeam( teamState, teamName, logoURL ) );
		}

		return result;
	}

	private String getTeamState( final String teamSection ) {

		final Pattern pattern = Pattern.compile( "<span>(.+?)</span>" );
		final Matcher matcher = pattern.matcher( teamSection );
		if ( matcher.find() ) {
			return matcher.group( 1 );
		}

		throw new IllegalStateException( String.format( "Can not extract team state: %s", teamSection ) );
	}

	private String getTeamName( final String teamSection ) {
		final Pattern pattern = Pattern.compile( "<span>(.+?)</span>" );
		final Matcher matcher = pattern.matcher( teamSection );
		matcher.find();
		if ( matcher.find() ) {
			return matcher.group( 1 );
		}

		return "";
//		throw new IllegalStateException( String.format( "Can not extract team name: %s", teamSection ) );
	}

	private String getLogo( final String teamSection ) {

		final Pattern pattern = Pattern.compile( "<img src=\"(.+?)\"" );
		final Matcher matcher = pattern.matcher( teamSection );
		if ( matcher.find() ) {
			return matcher.group( 1 );
		}

		throw new IllegalStateException( String.format( "Can not extract team logo: %s", teamSection ) );
	}

	public String getRemotePageContent( final String pageUrl ) {

		final DefaultHttpClient httpClient = new DefaultHttpClient();

		final HttpGet httpGet = new HttpGet( pageUrl );

		try {
			final ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute( httpGet, responseHandler ); // -XX:-LoopUnswitching
		} catch ( final IOException e ) {
			System.out.println( e );
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return null;
	}

	private class NCAATeam {

		private final String teamState;
		private final String logoURL;
		private final String teamName;

		private NCAATeam( final String teamState, final String teamName, final String logoURL ) {
			this.teamState = teamState.replace( "&#39;", "'" );
			this.teamName = teamName.replace( "&#39;", "'" );
			this.logoURL = logoURL;
		}
	}
}
