package betmen.core.service.admin;

import betmen.core.entity.Cup;

import java.util.List;

public interface AdminMnBService {

    List<Cup> loadAllCups();

    List<Cup> loadAllCurrentCups();
}
