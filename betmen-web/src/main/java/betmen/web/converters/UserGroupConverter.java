package betmen.web.converters;

import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.dto.dto.UserGroupEditDTO;

public interface UserGroupConverter {

    void populateEntity(UserGroupEntity entity, UserGroupEditDTO dto, User currentUser);

    UserGroupEditDTO convertToDto(UserGroupEntity entity);
}
