package betmen.core.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.UserGroupMember.LOAD_ALL;
import static betmen.core.entity.UserGroupMember.LOAD_USER_GROUPS_MEMBERS;
import static betmen.core.entity.UserGroupMember.LOAD_USER_GROUPS_MEMBER_ENTRY;
import static betmen.core.entity.UserGroupMember.LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "userGroupMembers")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select g from UserGroupMember g order by groupName"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_MEMBERS,
                query = "select g from UserGroupMember g where userGroupId= :userGroupId"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER,
                query = "select g from UserGroupMember g where userId= :userId"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_MEMBER_ENTRY,
                query = "select g from UserGroupMember g where userId= :userId and userGroupId= :userGroupId"
        )
})
public class UserGroupMember extends AbstractEntity {

    public static final String LOAD_ALL = "userGroupMembers.loadAll";
    public static final String LOAD_USER_GROUPS_MEMBERS = "userGroupMembers.loadMembers";
    public static final String LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER = "userGroupMembers.loadWhereUserIsMembers";
    public static final String LOAD_USER_GROUPS_MEMBER_ENTRY = "userGroupMembers.loadUserGroupMemberEntry";
    public static final String LOAD_USER_GROUPS_FOR_CUP_WHERE_WHERE_USER_IS_MEMBER_SQL = "select g.* " +
            " FROM userGroupMembers gm " +
            "  JOIN userGroups g ON (gm.userGroupId = g.id) " +
            "  JOIN userGroupCups gc ON (g.id = gc.userGroupId) " +
            " WHERE g.userId = :userId " +
            "      AND gc.cupId = :cupId" +
            " ORDER BY g.groupName";

    @ManyToOne
    @JoinColumn(name = "userGroupId")
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public UserGroupMember() {
    }

    public UserGroupMember(final UserGroup userGroup, final User user) {
        this.userGroup = userGroup;
        this.user = user;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        return 31 * userGroup.getId();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof UserGroupMember)) {
            return false;
        }

        final UserGroupMember userGroupMember = (UserGroupMember) obj;
        return userGroupMember.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%d: %s ( %s )", getId(), userGroup, user);
    }
}
