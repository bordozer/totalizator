package betmen.web.controllers.rest.admin.jobs.logs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobLogsDTO {
    private final List<JobLogDTO> jobTaskLogs;
    private JobTaskDTO jobTask;

    public JobLogsDTO(final List<JobLogDTO> jobTaskLogs) {
        this.jobTaskLogs = jobTaskLogs;
    }
}
