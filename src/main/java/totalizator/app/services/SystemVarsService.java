package totalizator.app.services;

import java.io.File;
import java.util.List;

public interface SystemVarsService {

	String getDatabaseHost();

	String getDatabasePort();

	String getDatabaseName();

	String getDatabaseUserName();

	String getDatabaseUserPassword();

	String getLogosPath();

	File getImportedGamesDataStorageDir();

	List<Integer> getAdminIds();
}
