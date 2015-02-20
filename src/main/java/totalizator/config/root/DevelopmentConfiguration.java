package totalizator.config.root;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import totalizator.app.services.SystemVarsService;
import totalizator.app.services.SystemVarsServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("development")
@EnableTransactionManagement
public class DevelopmentConfiguration {

	private static final Logger LOGGER = Logger.getLogger( DevelopmentConfiguration.class );

	/*@Bean( initMethod = "init" )
	public TestDataInitializer initTestData() {
		return new TestDataInitializer();
	}*/

	@Bean( name = "systemVarsService", initMethod = "init" )
	public SystemVarsServiceImpl systemVarsService() {
		return new SystemVarsServiceImpl();
	}

	@Bean( name = "datasource" )
	public DriverManagerDataSource dataSource( final SystemVarsService systemVarsService ) {

		LOGGER.debug( String.format( "Connection information: host=%s; port=%s; db=%s"
				, systemVarsService.getDatabaseHost()
				, systemVarsService.getDatabasePort()
				, systemVarsService.getDatabaseName() )
		);

		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName( "com.mysql.jdbc.Driver" );
		dataSource.setUrl( String.format( "jdbc:mysql://%s:%s/%s"
				, systemVarsService.getDatabaseHost()
				, systemVarsService.getDatabasePort()
				, systemVarsService.getDatabaseName() )
		);
		dataSource.setUsername( systemVarsService.getDatabaseUserName());
		dataSource.setPassword( systemVarsService.getDatabaseUserPassword() );

		return dataSource;
	}

	@Bean( name = "entityManagerFactory" )
	public LocalContainerEntityManagerFactoryBean entityManagerFactory( final DriverManagerDataSource dataSource ) {

		final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource( dataSource );
		entityManagerFactoryBean.setPackagesToScan( "totalizator.app.models" );
		entityManagerFactoryBean.setLoadTimeWeaver( new InstrumentationLoadTimeWeaver() );
		entityManagerFactoryBean.setJpaVendorAdapter( new HibernateJpaVendorAdapter() );

		final Map<String, Object> jpaProperties = new HashMap<String, Object>();
//		jpaProperties.put( "hibernate.hbm2ddl.auto", "create" );
		jpaProperties.put( "hibernate.show_sql", "true" );
		jpaProperties.put( "hibernate.format_sql", "true" );
		jpaProperties.put( "hibernate.use_sql_comments", "true" );
		jpaProperties.put( "hibernate.dialect", "org.hibernate.dialect.MySQLDialect" );
		entityManagerFactoryBean.setJpaPropertyMap( jpaProperties );

		return entityManagerFactoryBean;
	}
}
