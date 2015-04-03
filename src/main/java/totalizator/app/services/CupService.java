package totalizator.app.services;

import com.google.common.base.Function;
import totalizator.app.dto.CupDTO;
import totalizator.app.models.Cup;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	Function<Cup, CupDTO> CUP_DTO_FUNCTION = new Function<Cup, CupDTO>() {
		@Override
		public CupDTO apply( final Cup cup ) {
			final String cupName = String.format( "%s: %s", cup.getCategory().getCategoryName(), cup.getCupName() ); // TODO: hack with category name
			return new CupDTO( cup.getId(), cupName, cup.getCategory().getId() );
		}
	};

	List<Cup> portalPageCups();
}
