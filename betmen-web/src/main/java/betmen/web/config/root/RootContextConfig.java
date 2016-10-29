package betmen.web.config.root;

import betmen.core.service.SystemVarsServiceImpl;
import betmen.core.translator.TranslatorServiceImpl;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.ConfigurationFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

@Configuration
@EnableCaching // TODO: ENABLE
@ComponentScan({
        "betmen.core.service"
        , "betmen.core.repository"
        , "betmen.web.init"
        , "betmen.core.translator"
        , "betmen.web.security"
        , "betmen.web.converters"
})
@EnableJpaRepositories("betmen.core.repository.jpa")
public class RootContextConfig {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory, final DriverManagerDataSource dataSource) {

        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }

    @Bean(name = "systemVarsService", initMethod = "init")
    public SystemVarsServiceImpl systemVarsService() {
        return new SystemVarsServiceImpl();
    }

    @Bean(name = "translatorService", initMethod = "init")
    public TranslatorServiceImpl translatorService() {
        return new TranslatorServiceImpl();
    }

    @Bean
    public EhCacheCacheManager cacheManager() throws IOException {
        return new EhCacheCacheManager(CacheManager.create(ConfigurationFactory.parseConfiguration(getConfigLocation().getInputStream())));
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new MyKeyGenerator();
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {

        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");

        return resolver;
    }

    private Resource getConfigLocation() {
        return new FileSystemResource("src/main/webapp/WEB-INF/ehcache.xml");
    }
}
