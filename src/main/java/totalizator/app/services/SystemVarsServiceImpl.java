package totalizator.app.services;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class SystemVarsServiceImpl implements SystemVarsService {

	private static final Logger LOGGER = Logger.getLogger( SystemVarsServiceImpl.class );

	private final CompositeConfiguration config = new CompositeConfiguration();

	public void init() throws ConfigurationException, IOException {

		final List<String> propertyFiles = newArrayList();
		propertyFiles.add( "database" );
		propertyFiles.add( "system" );

		config.clear();

		for ( final String propertyFileName : propertyFiles ) {
			final File propertyFile = getPropertyFile( propertyFileName );

			LOGGER.debug( String.format( "Loading configuration from file '%s'", propertyFile.getCanonicalPath() ) );

			if ( ! propertyFile.exists() ) {
				final String message = String.format( "Property file '%s' does not exist!", propertyFile.getCanonicalPath() );
				LOGGER.error( message );
				throw new IOException( message );
			}

			config.addConfiguration( new PropertiesConfiguration( propertyFile ) );
		}

		LOGGER.debug( "Configurations have been loaded" );
	}

	@Override
	public String getDatabaseHost() {
		return config.getString( "database.host" );
	}

	@Override
	public String getDatabasePort() {
		return config.getString( "database.port" );
	}

	@Override
	public String getDatabaseName() {
		return config.getString( "database.name" );
	}

	@Override
	public String getDatabaseUserName() {
		return config.getString( "database.user.name" );
	}

	@Override
	public String getDatabaseUserPassword() {
		return config.getString( "database.user.password" );
	}

	@Override
	public String getLogosPath() {
		return config.getString( "system.logos.path" );
	}

	@Override
	public String getImportedGamesStatisticsPath() {
		return config.getString( "system.imports.path" );
	}

	private File getPropertyFile( final String fileName ) {
		return new File( String.format( "%s/%s.properties", getPropertiesPath(), fileName ) );
	}

	private String getPropertiesPath() {
		return "src/main/resources";
	}
}
