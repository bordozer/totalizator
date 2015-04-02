package totalizator.app.init.initializers;

import totalizator.app.models.Team;

import java.io.File;

public class TeamData {

	private final Team team;
	private File logo;

	public TeamData( final Team team ) {
		this.team = team;
	}

	public TeamData( final Team team, final File logo ) {
		this.team = team;
		this.logo = logo;
	}

	public Team getTeam() {
		return team;
	}

	public File getLogo() {
		return logo;
	}
}
