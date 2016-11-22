package betmen.web.converters;

import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupCupEntity;
import betmen.core.service.CupService;
import betmen.dto.dto.UserGroupEditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

@Component
public class UserGroupConverterImpl implements UserGroupConverter {

    @Autowired
    private CupService cupService;

    @Override
    public void populateEntity(final UserGroupEntity entity, final UserGroupEditDTO dto, final User currentUser) {
        entity.setOwner(currentUser);
        entity.setGroupName(dto.getUserGroupName());
        if (!CollectionUtils.isEmpty(dto.getCupIds())) {
            entity.setUserGroupCups(
                    dto.getCupIds().stream()
                            .map(cupId -> new UserGroupCupEntity(entity, cupService.loadAndAssertExists(cupId)))
                            .collect(Collectors.toList())
            );
        }
    }

    @Override
    public UserGroupEditDTO convertToDto(final UserGroupEntity entity) {
        final UserGroupEditDTO ret = new UserGroupEditDTO();
        ret.setUserGroupId(entity.getId());
        ret.setUserGroupName(entity.getGroupName());
        ret.setCupIds(entity.getUserGroupCups().stream().map(userGroupCup -> userGroupCup.getCup().getId()).collect(Collectors.toList()));
        return ret;
    }
}
