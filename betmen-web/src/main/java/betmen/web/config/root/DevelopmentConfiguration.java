package betmen.web.config.root;

import betmen.core.service.SystemVarsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.SharedCacheMode;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@Profile("development")
@EnableTransactionManagement
public class DevelopmentConfiguration {

	/*@Bean( initMethod = "init" )
    public TestDataInitializer initTestData() {
		return new TestDataInitializer();
	}*/

    @Autowired
    private SystemVarsService systemVarsService;

    @Bean(name = "datasource")
    public DriverManagerDataSource dataSource() {

        LOGGER.debug(String.format("Connection information: host=%s; port=%s; db=%s", systemVarsService.getDatabaseHost(), systemVarsService.getDatabasePort(), systemVarsService.getDatabaseName()));

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s", systemVarsService.getDatabaseHost(), systemVarsService.getDatabasePort(), systemVarsService.getDatabaseName()));
        dataSource.setUsername(systemVarsService.getDatabaseUserName());
        dataSource.setPassword(systemVarsService.getDatabaseUserPassword());

        return dataSource;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DriverManagerDataSource dataSource) {

        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("betmen.core.entity");
        entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        final Map<String, Object> jpaProperties = new HashMap<String, Object>();
//		jpaProperties.put( "hibernate.hbm2ddl.auto", "create" ); // TODO: for DB upgrade only
        jpaProperties.put("hibernate.connection.CharSet", "utf8");
        jpaProperties.put("hibernate.connection.characterEncoding", "utf8");
        jpaProperties.put("hibernate.connection.useUnicode", "true");
        jpaProperties.put("hibernate.show_sql", "false");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.use_sql_comments", "true");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

//		jpaProperties.put( "hibernate.order_updates", "true" );
//		jpaProperties.put( "hibernate.order_inserts", "true" );

        jpaProperties.put("javax.persistence.sharedCache.mode", SharedCacheMode.ENABLE_SELECTIVE);
        jpaProperties.put(Environment.CACHE_REGION_FACTORY, EhCacheRegionFactory.class.getName());
        jpaProperties.put(Environment.USE_SECOND_LEVEL_CACHE, true);
        jpaProperties.put(Environment.USE_QUERY_CACHE, true);
        jpaProperties.put(Environment.GENERATE_STATISTICS, true);
        jpaProperties.put(Environment.USE_STRUCTURED_CACHE, true);

        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PersistentTokenRepository rememberMeTokenRepository() {

        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource());
//		jdbcTokenRepository.setCreateTableOnStartup( true ); // TODO: for DB upgrade only

        return jdbcTokenRepository;
    }
}
