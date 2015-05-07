package totalizator.config.root;

import org.apache.log4j.Logger;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import totalizator.app.init.TestDataInitializer;
import totalizator.app.services.SystemVarsService;
import totalizator.app.services.SystemVarsServiceImpl;
import totalizator.app.translator.TranslatorServiceImpl;

import javax.persistence.SharedCacheMode;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile( "development" )
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

	@Bean( name = "translatorService", initMethod = "init" )
	public TranslatorServiceImpl TranslatorService() {
		return new TranslatorServiceImpl();
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
		jpaProperties.put( "hibernate.connection.CharSet", "utf8" );
		jpaProperties.put( "hibernate.connection.characterEncoding", "utf8" );
		jpaProperties.put( "hibernate.connection.useUnicode", "true" );
		jpaProperties.put( "hibernate.show_sql", "true" );
		jpaProperties.put( "hibernate.format_sql", "true" );
		jpaProperties.put( "hibernate.use_sql_comments", "true" );
		jpaProperties.put( "hibernate.dialect", "org.hibernate.dialect.MySQLDialect" );

		jpaProperties.put( "javax.persistence.sharedCache.mode", SharedCacheMode.ENABLE_SELECTIVE );
		jpaProperties.put( Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getName() );
		jpaProperties.put( Environment.USE_SECOND_LEVEL_CACHE, true );
		jpaProperties.put( Environment.USE_QUERY_CACHE, true );
		jpaProperties.put( Environment.GENERATE_STATISTICS, true );
		jpaProperties.put( Environment.USE_STRUCTURED_CACHE, true );

		entityManagerFactoryBean.setJpaPropertyMap( jpaProperties );

		return entityManagerFactoryBean;
	}

	@Bean( name = "ehCacheManager" )
	public CacheManager cacheManager() {
		return new EhCacheCacheManager( ehcache().getObject() );
	}

	@Bean( name = "ehcache" )
	public EhCacheManagerFactoryBean ehcache() {

		final EhCacheManagerFactoryBean ehcache = new EhCacheManagerFactoryBean();
		ehcache.setCacheManagerName( "ehCacheManager" );

		return ehcache;
	}
}
