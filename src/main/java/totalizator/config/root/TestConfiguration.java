package totalizator.config.root;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import totalizator.app.init.TestDataInitializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
@EnableTransactionManagement
public class TestConfiguration {

	@Bean(initMethod = "init")
	public TestDataInitializer initTestData() {
		return new TestDataInitializer();
	}

	@Bean( name = "datasource" )
	public DriverManagerDataSource dataSource() {

		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName( org.hsqldb.jdbcDriver.class.getName() );
		dataSource.setUrl( "jdbc:hsqldb:mem:mydb" );
		dataSource.setUsername( "sa" );
		dataSource.setPassword( "jdbc:hsqldb:mem:mydb" );
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
		jpaProperties.put( "hibernate.hbm2ddl.auto", "create" );
		jpaProperties.put( "hibernate.show_sql", "true" );
		jpaProperties.put( "hibernate.format_sql", "true" );
		jpaProperties.put( "hibernate.use_sql_comments", "true" );
		entityManagerFactoryBean.setJpaPropertyMap( jpaProperties );

		return entityManagerFactoryBean;
	}
}
