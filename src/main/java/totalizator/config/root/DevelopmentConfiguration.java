package totalizator.config.root;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;
import org.apache.log4j.Logger;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import totalizator.app.cache.MyKeyGenerator;
import totalizator.app.services.SystemVarsService;

import javax.persistence.SharedCacheMode;
import java.io.IOException;
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
		dataSource.setUsername( systemVarsService.getDatabaseUserName() );
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
		jpaProperties.put( Environment.CACHE_REGION_FACTORY, EhCacheRegionFactory.class.getName() );
		jpaProperties.put( Environment.USE_SECOND_LEVEL_CACHE, true );
		jpaProperties.put( Environment.USE_QUERY_CACHE, true );
		jpaProperties.put( Environment.GENERATE_STATISTICS, true );
		jpaProperties.put( Environment.USE_STRUCTURED_CACHE, true );
//		jpaProperties.put( Environment.CACHE_PROVIDER_CONFIG, "src/main/webapp/WEB-INF/ehcache.xml" );

		entityManagerFactoryBean.setJpaPropertyMap( jpaProperties );

		return entityManagerFactoryBean;
	}

	@Bean
	public EhCacheCacheManager cacheManager() throws IOException {
		return new EhCacheCacheManager( CacheManager.create( ConfigurationFactory.parseConfiguration( getConfigLocation().getInputStream() ) ) );
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new MyKeyGenerator();
	}

	private Resource getConfigLocation() {
		return new FileSystemResource( "src/main/webapp/WEB-INF/ehcache.xml" );
	}
}
