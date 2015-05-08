package totalizator.config.root;

import net.sf.ehcache.config.Configuration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class EhCacheConfigurationFactory  implements FactoryBean<Configuration>, InitializingBean {

	private Configuration configuration;

	@Override
	public Configuration getObject() throws Exception {
		return configuration;
	}

	@Override
	public Class<?> getObjectType() {
		return ( this.configuration != null ? this.configuration.getClass() : Configuration.class );
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	private Resource setConfigLocation() {
		return new FileSystemResource( "src/main/webapp/WEB-INF/ehcache.xml" );
	}
}
