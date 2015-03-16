package totalizator.app.init;

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

	private static final String NBA = "src/main/java/totalizator/app/init/teams/nba/teams.xml";
	private static final String NCAA = "src/main/java/totalizator/app/init/teams/ncaa/teams.xml";
	private static final String UEFA = "src/main/java/totalizator/app/init/teams/uefa/teams.xml";

	List<Team> importNBA( final Category category ) throws DocumentException {
		return doImport( NBA, category );
	}

	List<Team> importNCAA( final Category category ) throws DocumentException {
		return doImport( NCAA, category );
	}

	List<Team> importUEFA( final Category category ) throws DocumentException {
		return doImport( UEFA, category );
	}

	private List<Team> doImport( final String file, final Category category ) throws DocumentException {

		final SAXReader reader = new SAXReader( false );
		final Document document = reader.read( new File( file ) );

		final List<Team> result = newArrayList();

		final Iterator iterator = document.getRootElement().elementIterator( "team" );
		while ( iterator.hasNext() ) {

			final Element teamElement = ( Element ) iterator.next();
			final String name = teamElement.element( "name" ).getText();
//			final String logoImageName = teamElement.element( "logoImageName" ).getText();

			result.add( new Team( name, category ) );
		}

		return result;
	}
}
