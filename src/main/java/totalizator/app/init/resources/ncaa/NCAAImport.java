package totalizator.app.init.resources.ncaa;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class NCAAImport {

	public static void main( String[] args ) {
		final NCAAImport importer = new NCAAImport();
		importer.makeImport();
	}

	private void makeImport() {

		final String content = getRemotePageContent( "http://www.foxsports.com/college-basketball/teams" );

		final List<NCAATeam> teams = getTeams( content );
	}

	private List<NCAATeam> getTeams( final String content ) {
		final List<NCAATeam> result = newArrayList();

		final Pattern pattern = Pattern.compile( "<div class=\"wisfb_fullTeamStacked\">(.+?)</div>" );
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
		if ( matcher.find() ) {
			return matcher.group( 2 );
		}

		throw new IllegalStateException( String.format( "Can not extract team name: %s", teamSection ) );
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

		private NCAATeam( final String teamState, final String logoURL, final String teamName ) {
			this.teamState = teamState;
			this.logoURL = logoURL;
			this.teamName = teamName;
		}
	}
}
