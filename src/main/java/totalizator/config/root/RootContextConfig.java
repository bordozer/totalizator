package totalizator.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan( { "totalizator.app.dao", "totalizator.app.controllers" } )
public class RootContextConfig {

	// All beans are going to be here
}
