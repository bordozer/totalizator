package totalizator.app.controllers.rest.admin.sportKinds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.SportKind;
import totalizator.app.services.SportKindService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/admin/rest/sport-kinds" )
public class AdminSportKindRestController {

	@Autowired
	private SportKindService sportKindService;

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public List<SportKindEditDTO> all() {

		return sportKindService.loadAll()
				.stream()
				.map( getSportKindToDTOMapper() )
				.collect( Collectors.toList() );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/0" )
	public SportKindEditDTO newSportKind( final @RequestBody SportKindEditDTO dto ) {

		final SportKind sportKind = new SportKind();
		sportKind.setSportKindName( dto.getSportKindName() );

		final SportKind saved = sportKindService.save( sportKind );

		return getSportKindToDTOMapper().apply( saved );
	}

	@RequestMapping( method = RequestMethod.PUT, value = "/{sportKindId}" )
	public SportKindEditDTO editSportKind( final @PathVariable( "sportKindId" ) int sportKindId, final @RequestBody SportKindEditDTO dto ) {

		final SportKind sportKind = sportKindService.load( dto.getSportKindId() );
		sportKind.setSportKindName( dto.getSportKindName() );

		sportKindService.save( sportKind );

		return dto;
	}

	@RequestMapping( method = RequestMethod.DELETE, value = "/{sportKindId}" )
	public boolean delete( final @PathVariable( "sportKindId" ) int sportKindId ) {

		if ( sportKindId == 0 ) {
			return true;
		}

		// TODO: check id some category has it assigned

		sportKindService.delete( sportKindId );

		return true;
	}

	private Function<SportKind, SportKindEditDTO> getSportKindToDTOMapper() {

		return new Function<SportKind, SportKindEditDTO>() {

			@Override
			public SportKindEditDTO apply( final SportKind sportKind ) {
				return new SportKindEditDTO( sportKind.getId(), sportKind.getSportKindName() );
			}
		};
	}
}
