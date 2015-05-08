package totalizator.config.root;

import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import totalizator.app.services.SystemVarsServiceImpl;
import totalizator.app.translator.TranslatorServiceImpl;

import javax.persistence.EntityManagerFactory;

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
	public EhCacheCacheManager cacheManager() {
		return new EhCacheCacheManager( ehCacheManagerFactoryBean().getObject() );
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {

		final EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		cacheManagerFactoryBean.setConfigLocation( new FileSystemResource( "src/main/webapp/WEB-INF/ehcache.xml" ) );
		cacheManagerFactoryBean.setShared( true );

		return cacheManagerFactoryBean;
	}
}
