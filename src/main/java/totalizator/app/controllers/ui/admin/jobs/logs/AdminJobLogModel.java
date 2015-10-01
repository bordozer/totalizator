package totalizator.app.controllers.ui.admin.jobs.logs;

import totalizator.app.controllers.ui.AbstractPageModel;

public class AdminJobLogModel extends AbstractPageModel {

	private int jobTaskId;

	public void setJobTaskId( final int jobTaskId ) {
		this.jobTaskId = jobTaskId;
	}

	public int getJobTaskId() {
		return jobTaskId;
	}
}
