package totalizator.app.controllers.rest.admin.jobs;

import totalizator.app.controllers.rest.admin.jobs.parameters.RemoteGamesImportJobTaskParametersDTO;

public class JobTaskGamesImportEditDTO extends JobTaskEditDTO {

	private RemoteGamesImportJobTaskParametersDTO jobTaskParametersHolder;

	public RemoteGamesImportJobTaskParametersDTO getJobTaskParametersHolder() {
		return jobTaskParametersHolder;
	}

	public void setJobTaskParametersHolder( final RemoteGamesImportJobTaskParametersDTO jobTaskParametersHolder ) {
		this.jobTaskParametersHolder = jobTaskParametersHolder;
	}
}
