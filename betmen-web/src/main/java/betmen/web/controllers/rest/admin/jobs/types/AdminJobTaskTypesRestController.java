package betmen.web.controllers.rest.admin.jobs.types;

import betmen.core.model.AppContext;
import betmen.core.service.jobs.JobTaskType;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/admin/rest/job-tasks/types")
public class AdminJobTaskTypesRestController {

    @Autowired
    private TranslatorService translatorService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<JobTaskTypeDTO> jobTaskTypes(final HttpServletRequest request) {
        final Language language = AppContext.read(request.getSession()).getLanguage();
        return newArrayList(JobTaskType.values())
                .stream()
                .map(jobTaskType -> new JobTaskTypeDTO(jobTaskType.getId(), translatorService.translate(jobTaskType.getName(), language)))
                .collect(Collectors.toList());
    }
}
