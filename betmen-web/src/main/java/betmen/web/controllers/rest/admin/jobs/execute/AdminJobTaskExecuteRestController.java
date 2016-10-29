package betmen.web.controllers.rest.admin.jobs.execute;

import betmen.core.service.jobs.execution.JobTaskExecutionService;
import betmen.core.service.jobs.executors.AbstractJobTaskExecutor;
import betmen.core.service.jobs.executors.JobTaskExecutionMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/rest/job-tasks/execution/")
public class AdminJobTaskExecuteRestController {

    @Autowired
    private JobTaskExecutionService jobTaskExecutionService;

    @RequestMapping(method = RequestMethod.POST, value = "/run/{jobTaskId}/")
    public void runExecution(final @PathVariable int jobTaskId) {
        // check if job task is already executing?
        jobTaskExecutionService.execute(jobTaskId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/stop/{jobTaskId}/")
    public void stopExecution(final @PathVariable int jobTaskId) {
        jobTaskExecutionService.stop(jobTaskId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/status/{jobTaskId}/")
    public JobTaskExecutionMonitorDTO executionProgress(final @PathVariable int jobTaskId) {
        return getJobTaskExecutionMonitorDTO(jobTaskId);
    }

    private JobTaskExecutionMonitorDTO getJobTaskExecutionMonitorDTO(final int jobTaskId) {
        final AbstractJobTaskExecutor jobExecutor = jobTaskExecutionService.getJobExecutor(jobTaskId);
        if (jobExecutor == null) {
            final JobTaskExecutionMonitorDTO monitorDTO = new JobTaskExecutionMonitorDTO();
            monitorDTO.setJobExecutionStateId(-1);
            return monitorDTO;
        }
        final JobTaskExecutionMonitor executionMonitor = jobExecutor.getExecutionMonitor();

        final JobTaskExecutionMonitorDTO dto = new JobTaskExecutionMonitorDTO();
        dto.setTotalSteps(executionMonitor.getTotalSteps());
        dto.setCurrentStep(executionMonitor.getCurrentStep());
        dto.setStartTime(executionMonitor.getStartTime());
        dto.setFinishingTime(executionMonitor.getFinishingTime());
        dto.setJobExecutionStateId(jobExecutor.getJobExecutionState().getId());
        dto.setJobExecuting(!jobExecutor.isStoppedByAnyReason());

        return dto;
    }
}
