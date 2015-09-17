package totalizator.app.controllers.rest.sportKindCups;

import java.util.List;

public class SportKindsCupsDTO {

	private final List<SportKindCupsDTO> sportKindCups;

	public SportKindsCupsDTO( final List<SportKindCupsDTO> sportKindCups ) {
		this.sportKindCups = sportKindCups;
	}

	public List<SportKindCupsDTO> getSportKindCups() {
		return sportKindCups;
	}
}
