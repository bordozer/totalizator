package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupCupEntity;
import betmen.core.entity.UserGroupMemberEntity;
import betmen.core.repository.UserGroupCupDao;
import betmen.core.repository.UserGroupDao;
import betmen.core.repository.UserGroupMemberDao;
import betmen.core.repository.jpa.UserGroupJpaRepository;
import betmen.core.service.points.MatchPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupDao userGroupRepository;
    @Autowired
    private UserGroupCupDao userGroupCupRepository;
    @Autowired
    private UserGroupMemberDao userGroupMemberRepository;
    @Autowired
    private MatchPointsService matchPointsService;
    @Autowired
    private UserGroupJpaRepository userGroupJpaRepository;

    @Override
    public List<UserGroupEntity> loadAll() {
        return userGroupRepository.loadAll();
    }

    @Override
    public UserGroupEntity load(final int id) {
        return userGroupRepository.load(id);
    }

    @Override
    @Transactional
    public UserGroupEntity save(final UserGroupEntity entry) {
        return userGroupRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        UserGroupEntity userGroupEntity = load(id);
        userGroupRepository.delete(id);
        matchPointsService.delete(userGroupEntity);
    }

    @Override
    public List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final User user) {
        return userGroupJpaRepository.findAllByOwnerIdOrderByGroupNameAsc(user.getId());
    }

    @Override
    public List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final User user, final int cupId) {
        return userGroupRepository.loadUserGroupsWhereUserIsOwner(user.getId(), cupId);
    }

    @Override
    public List<UserGroupEntity> loadUserGroupsWhereUserIsMember(final User user) {
        return userGroupMemberRepository.loadUserGroupsWhereUserIsMember(user.getId())
                .stream()
                .map(UserGroupMemberEntity::getUserGroup)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserGroupEntity> loadUserGroupsWhereUserIsMember(final User user, final int cupId) {
        return userGroupMemberRepository.loadUserGroupsWhereUserIsMember(user.getId(), cupId)
                .stream()
                .map(UserGroupMemberEntity::getUserGroup)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> loadUserGroupMembers(final UserGroupEntity userGroupEntity) {
        final List<UserGroupMemberEntity> userGroupMemberEntities = newArrayList();
        userGroupMemberEntities.add(new UserGroupMemberEntity(userGroupEntity, userGroupEntity.getOwner()));
        userGroupMemberEntities.addAll(userGroupMemberRepository.loadUserGroupMembers(userGroupEntity));
        return userGroupMemberEntities.stream().map(UserGroupMemberEntity::getUser).collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadCups(final UserGroupEntity userGroupEntity) {
        return userGroupCupRepository.loadCups(userGroupEntity).stream().map(UserGroupCupEntity::getCup).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = GenericService.CACHE_PERMANENT)
    public boolean isUserOwnerOfGroup(final UserGroupEntity userGroupEntity, final User user) {
        if (userGroupEntity == null) {
            return true;
        }
        for (final UserGroupEntity group : loadUserGroupsWhereUserIsOwner(user)) {
            if (userGroupEntity.equals(group) && group.getOwner().equals(user)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isUserMemberOfGroup(final UserGroupEntity userGroupEntity, final User user) {

        if (isUserOwnerOfGroup(userGroupEntity, user)) {
            return true;
        }

        return loadUserGroupsWhereUserIsMember(user)
                .stream()
                .anyMatch(memberOfGroup -> memberOfGroup.equals(userGroupEntity));
    }

    @Override
    public void addMember(final UserGroupEntity userGroupEntity, final User user) {
        userGroupMemberRepository.save(new UserGroupMemberEntity(userGroupEntity, user));
    }

    @Override
    @Transactional
    public void removeMember(final UserGroupEntity userGroupEntity, final User user) {
        userGroupMemberRepository.delete(userGroupEntity, user);
    }
}
