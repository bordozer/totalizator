package totalizator.config.root;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile( "development" )
@EnableTransactionManagement
public class DevelopmentConfiguration {

}
