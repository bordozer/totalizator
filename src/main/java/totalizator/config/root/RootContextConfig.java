package totalizator.config.root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

@Configuration
@ComponentScan( { "totalizator.app.services", "totalizator.app.dao", "totalizator.app.init", "totalizator.app.security", "totalizator.app.translator" } )
public class RootContextConfig {

	@Bean( name = "transactionManager" )
	public PlatformTransactionManager transactionManager( final EntityManagerFactory entityManagerFactory, final DriverManagerDataSource dataSource ) {

		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory( entityManagerFactory );
		transactionManager.setDataSource( dataSource );

		return transactionManager;
	}
}
