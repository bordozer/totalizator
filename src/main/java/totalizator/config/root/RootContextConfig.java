package totalizator.config.root;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import totalizator.app.services.SystemVarsServiceImpl;
import totalizator.app.translator.TranslatorServiceImpl;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

@Configuration
@EnableCaching
@ComponentScan( {
		"totalizator.app.services"
		, "totalizator.app.dao"
		, "totalizator.app.init"
		, "totalizator.app.security"
		, "totalizator.app.translator"
} )
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
}
