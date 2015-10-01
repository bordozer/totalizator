package totalizator.app.controllers.rest.admin.jobs.types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.beans.AppContext;
import totalizator.app.services.jobs.JobTaskType;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping( "/admin/rest/job-tasks/types" )
public class AdminJobTaskTypesRestController {

	@Autowired
	private TranslatorService translatorService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<JobTaskTypeDTO> jobTaskTypes( final HttpServletRequest request ) {

		final Language language = AppContext.read( request.getSession() ).getLanguage();

		return newArrayList( JobTaskType.values() )
				.stream()
				.map( new Function<JobTaskType, JobTaskTypeDTO>() {

					@Override
					public JobTaskTypeDTO apply( final JobTaskType jobTaskType ) {
						return new JobTaskTypeDTO( jobTaskType.getId(), translatorService.translate( jobTaskType.getName(), language ) );
					}
				} )
				.collect( Collectors.toList() );
	}
}
