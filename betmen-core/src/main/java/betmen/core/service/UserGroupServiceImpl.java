package betmen.core.service;

import betmen.core.repository.UserGroupCupDao;
import betmen.core.repository.UserGroupDao;
import betmen.core.repository.UserGroupMemberDao;
import betmen.core.entity.Cup;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.entity.UserGroupCup;
import betmen.core.entity.UserGroupMember;
import betmen.core.service.points.MatchPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
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
    private CupService cupService;

    @Autowired
    private MatchPointsService matchPointsService;

    @Override
    @Transactional(readOnly = true)
    public List<UserGroup> loadAll() {
        return userGroupRepository.loadAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserGroup load(final int id) {
        return userGroupRepository.load(id);
    }

    @Override
    @Transactional
    public UserGroup save(final UserGroup entry) {
        return userGroupRepository.save(entry);
    }

    @Override
    @Transactional
    public UserGroup save(final UserGroup userGroup, final List<Integer> cupIds) {

        final UserGroup saved = this.save(userGroup);

        userGroupCupRepository.deleteAll(saved);

        for (final int cupId : cupIds) {
            userGroupCupRepository.save(new UserGroupCup(saved, cupService.load(cupId)));
        }

        return saved;
    }

    @Override
    @Transactional
    public void delete(final int id) {

        final UserGroup userGroup = load(id);

        userGroupMemberRepository.deleteAll(userGroup);

        userGroupCupRepository.deleteAll(userGroup);

        userGroupRepository.delete(id);

        matchPointsService.delete(load(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroup> loadUserGroupsWhereUserIsOwner(final User user) {
        return userGroupRepository.loadUserGroupsWhereUserIsOwner(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroup> loadUserGroupsWhereUserIsMember(final User user) {

        return userGroupMemberRepository.loadUserGroupsWhereUserIsMember(user)
                .stream()
                .map(new Function<UserGroupMember, UserGroup>() {
                    @Override
                    public UserGroup apply(final UserGroupMember userGroupMember) {
                        return userGroupMember.getUserGroup();
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> loadUserGroupMembers(final UserGroup userGroup) {

        final Function<UserGroupMember, User> mapper = new Function<UserGroupMember, User>() {

            @Override
            public User apply(final UserGroupMember userGroupMember) {
                return userGroupMember.getUser();
            }
        };

        final List<UserGroupMember> userGroupMembers = newArrayList();
        userGroupMembers.add(new UserGroupMember(userGroup, userGroup.getOwner()));
        userGroupMembers.addAll(userGroupMemberRepository.loadUserGroupMembers(userGroup));

        return userGroupMembers.stream().map(mapper).collect(Collectors.toList());
    }

    @Override
    public List<Cup> loadCups(final UserGroup userGroup) {

        final Function<UserGroupCup, Cup> mapper = new Function<UserGroupCup, Cup>() {
            @Override
            public Cup apply(final UserGroupCup userGroupCup) {
                return userGroupCup.getCup();
            }
        };

        final List<UserGroupCup> userGroupCups = userGroupCupRepository.loadCups(userGroup);

        return userGroupCups.stream().map(mapper).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = GenericService.CACHE_PERMANENT)
    public boolean isUserOwnerOfGroup(final UserGroup userGroup, final User user) {

        if (userGroup == null) {
            return true;
        }

        for (final UserGroup group : loadUserGroupsWhereUserIsOwner(user)) {
            if (userGroup.equals(group) && group.getOwner().equals(user)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isUserMemberOfGroup(final UserGroup userGroup, final User user) {

        if (isUserOwnerOfGroup(userGroup, user)) {
            return true;
        }

        return loadUserGroupsWhereUserIsMember(user)
                .stream()
                .anyMatch(new Predicate<UserGroup>() {
                    @Override
                    public boolean test(final UserGroup memberOfGroup) {
                        return memberOfGroup.equals(userGroup);
                    }
                });
    }

    @Override
    @Transactional
    public void addMember(final UserGroup userGroup, final User user) {
        userGroupMemberRepository.save(new UserGroupMember(userGroup, user));
    }

    @Override
    @Transactional
    public void removeMember(final UserGroup userGroup, final User user) {
        userGroupMemberRepository.delete(userGroup, user);
    }
}