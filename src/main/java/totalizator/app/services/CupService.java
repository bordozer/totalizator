package totalizator.app.services;

import totalizator.app.models.Cup;

import java.util.List;

public interface CupService extends GenericService<Cup>, NamedEntityGenericService<Cup> {

	List<Cup> portalPageCups();
}
