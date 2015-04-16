package totalizator.app.services;

public interface SystemVarsService {

	String getDatabaseHost();

	String getDatabasePort();

	String getDatabaseName();

	String getDatabaseUserName();

	String getDatabaseUserPassword();

	String getLogosPath();

	String getImportedGamesStatisticsPath();
}
