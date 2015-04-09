package totalizator.app.init.initializers;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
class TeamImportService {

	private static final File RESOURCES_DIR = new File( "src/main/java/totalizator/app/init/teams/" );
	private static final String TEAMS_XML = "teams.xml";
	private static final String LOGOS_DIR_NAME = "logos";

	private static final String NBA = "nba";
	private static final String NCAA = "ncaa";
	private static final String UEFA = "uefa";

	public List<TeamData> importNBA( final Category category ) throws DocumentException {
		return doImport( NBA, category );
	}

	public List<TeamData> importNCAA( final Category category ) throws DocumentException {
		return doImport( NCAA, category );
	}

	public List<TeamData> importUEFA( final Category category ) throws DocumentException {
		return doImport( UEFA, category );
	}

	private List<TeamData> doImport( final String file, final Category category ) throws DocumentException {

		final SAXReader reader = new SAXReader( false );
		final String path = String.format( "%s/%s", RESOURCES_DIR, file );
		final Document document = reader.read( new File( path, TEAMS_XML ) );

		final List<TeamData> result = newArrayList();

		final Iterator iterator = document.getRootElement().elementIterator( "team" );
		while ( iterator.hasNext() ) {

			final Element teamElement = ( Element ) iterator.next();
			final String teamName = teamElement.element( "name" ).getText();

			final Team team = new Team( teamName, category );

			final Element logoElement = teamElement.element( "logo" );
			if ( logoElement == null ) {
				result.add( new TeamData( team ) );
				continue;
			}

			File logo = null;
			final String logoFileName = logoElement.getText();
			if ( StringUtils.isNotEmpty( logoFileName ) ) {
				final File logosDir = new File( path, LOGOS_DIR_NAME );
				logo = new File( logosDir, logoFileName );
				team.setLogoFileName( logoFileName );
			}

			result.add( new TeamData( team, logo ) );
		}

		return result;
	}
}
