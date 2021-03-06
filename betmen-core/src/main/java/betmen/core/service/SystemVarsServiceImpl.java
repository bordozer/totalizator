package betmen.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Service
public class SystemVarsServiceImpl implements SystemVarsService {

    private final CompositeConfiguration config = new CompositeConfiguration();

    private final List<Integer> adminIds = newArrayList();

    public void init() throws ConfigurationException, IOException {

        final List<String> propertyFiles = newArrayList();
        propertyFiles.add("database");
        propertyFiles.add("system");

        config.clear();

        for (final String propertyFileName : propertyFiles) {
            final File propertyFile = getPropertyFile(propertyFileName);

            LOGGER.debug(String.format("Loading configuration from file '%s'", propertyFile.getCanonicalPath()));

            if (!propertyFile.exists()) {
                final String message = String.format("Property file '%s' does not exist!", propertyFile.getCanonicalPath());
                LOGGER.error(message);
                throw new IOException(message);
            }

            config.addConfiguration(new PropertiesConfiguration(propertyFile));
        }

        final List<Object> _ids = config.getList("system.admin.ids");
        for (final Object _id : _ids) {
            adminIds.add(Integer.valueOf(((String) _id).trim()));
        }

        LOGGER.debug("Configurations have been loaded");
    }

    @Override
    public String getProjectName() {
        return config.getString("system.projectName");
    }

    @Override
    public String getDatabaseHost() {
        return config.getString("database.host");
    }

    @Override
    public String getDatabasePort() {
        return config.getString("database.port");
    }

    @Override
    public String getDatabaseName() {
        return config.getString("database.name");
    }

    @Override
    public String getDatabaseUserName() {
        return config.getString("database.user.name");
    }

    @Override
    public String getDatabaseUserPassword() {
        return config.getString("database.user.password");
    }

    @Override
    public String getLogosPath() {
        return config.getString("system.logos.path");
    }

    @Override
    public File getImportedGamesDataStorageDir() {
        return new File(config.getString("system.imports.path"));
    }

    @Override
    public List<Integer> getAdminIds() {
        return adminIds;
    }

    private File getPropertyFile(final String fileName) {
        return new File(String.format("%s/%s.properties", getPropertiesPath(), fileName));
    }

    private String getPropertiesPath() {
        return "src/main/resources";
    }
}
