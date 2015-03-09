package totalizator.app.controllers.rest.portal;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/portal-page" )
public class PortalPageRestController {

	private static final Logger LOGGER = Logger.getLogger( PortalPageRestController.class );

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public PortalPageDTO getDefaultLogin( final Principal principal ) {

		final User user = userService.findByLogin( principal.getName() );

		final PortalPageDTO portalPageDTO = new PortalPageDTO();

		portalPageDTO.setUserId( user.getId() );
		portalPageDTO.setUserName( user.getUsername() );

		final List<Cup> portalPageCups = cupService.loadAll();
		CollectionUtils.filter( portalPageCups, new Predicate<Cup>() {
			@Override
			public boolean evaluate( final Cup cup ) {
				return cup.isShowOnPortalPage();
			}
		} );

		portalPageDTO.setCupsShowTo( Lists.transform( portalPageCups, new Function<Cup, CupDTO>() {
			@Override
			public CupDTO apply( final Cup cup ) {
				return new CupDTO( cup.getId(), cup.getCupName(), cup.getCategory().getId() );
			}
		} ) );

		return portalPageDTO;
	}
}
