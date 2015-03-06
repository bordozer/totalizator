package totalizator.app.controllers.rest.matches;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;
import totalizator.app.services.MatchService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/matches" )
public class PortalPageMatchesRestController {

	@Autowired
	private MatchService matchService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/open/", produces = APPLICATION_JSON_VALUE )
	public List<MatchDTO> entries( final Principal principal ) {

		return Lists.transform( matchService.loadAll(), new Function<Match, MatchDTO>() {
			@Override
			public MatchDTO apply( final Match match ) {
				return matchService.initDTOFromModel( match );
			}
		} );
	}
}
