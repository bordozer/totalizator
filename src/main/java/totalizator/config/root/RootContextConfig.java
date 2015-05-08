package totalizator.config.root;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.FactoryConfiguration;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import totalizator.app.services.SystemVarsServiceImpl;
import totalizator.app.services.score.CupScoresService;
import totalizator.app.translator.TranslatorServiceImpl;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.List;

@Configuration
@ComponentScan( {
		"totalizator.app.services"
		, "totalizator.app.dao"
		, "totalizator.app.init"
		, "totalizator.app.security"
		, "totalizator.app.translator"
} )
//@EnableCaching
public class RootContextConfig {

	@Bean( name = "transactionManager" )
	public PlatformTransactionManager transactionManager( final EntityManagerFactory entityManagerFactory, final DriverManagerDataSource dataSource ) {

		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( entityManagerFactory );
		transactionManager.setDataSource( dataSource );

		return transactionManager;
	}

	@Bean( name = "systemVarsService", initMethod = "init" )
	public SystemVarsServiceImpl systemVarsService() {
		return new SystemVarsServiceImpl();
	}

	@Bean( name = "translatorService", initMethod = "init" )
	public TranslatorServiceImpl TranslatorService() {
		return new TranslatorServiceImpl();
	}

	@Bean
	public EhCacheCacheManager cacheManager() throws IOException {
		return new EhCacheCacheManager( CacheManager.create( ConfigurationFactory.parseConfiguration( getConfigLocation().getInputStream() ) ) );
	}

	private Resource getConfigLocation() {
		return new FileSystemResource( "src/main/webapp/WEB-INF/ehcache.xml" );
	}

	/*@Bean
	public EhCacheCacheManager cacheManager() throws IOException {

		final CacheConfiguration cupScoresCache = new CacheConfiguration( CupScoresService.CACHE_QUERY, 100 );

		final net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
		configuration.addCache( cupScoresCache );

		final List<FactoryConfiguration> factoryConfiguration = configuration.getCacheManagerPeerProviderFactoryConfiguration();
		if ( factoryConfiguration.iterator().hasNext() ) {
			final FactoryConfiguration peerProviderConfiguration = factoryConfiguration.iterator().next();
			peerProviderConfiguration.setProperties( "peerDiscovery=manual,rmiUrls=//localhost:#41001/totalizator.app.cache.cup-scores.query" );
			peerProviderConfiguration.setPropertySeparator( "," );
		}

		return new EhCacheCacheManager( CacheManager.create( configuration ) );
	}*/

	/*@Bean
	public EhCacheCacheManager cacheManager() {
		return new EhCacheCacheManager( ehCacheManagerFactoryBean().getObject() );
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

		final EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		cacheManagerFactoryBean.setConfigLocation( new FileSystemResource( "src/main/webapp/WEB-INF/ehcache.xml" ) );
		cacheManagerFactoryBean.setShared( true );

		return cacheManagerFactoryBean;
	}*/
}
