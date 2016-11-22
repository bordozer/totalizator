package betmen.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static betmen.core.entity.UserGroupMemberEntity.LOAD_ALL;
import static betmen.core.entity.UserGroupMemberEntity.LOAD_USER_GROUPS_MEMBERS;
import static betmen.core.entity.UserGroupMemberEntity.LOAD_USER_GROUPS_MEMBER_ENTRY;
import static betmen.core.entity.UserGroupMemberEntity.LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "userGroupMembers")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select g from UserGroupMemberEntity g order by groupName"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_MEMBERS,
                query = "select g from UserGroupMemberEntity g where userGroupId= :userGroupId"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER,
                query = "select g from UserGroupMemberEntity g where userId= :userId"
        ),
        @NamedQuery(
                name = LOAD_USER_GROUPS_MEMBER_ENTRY,
                query = "select g from UserGroupMemberEntity g where userId= :userId and userGroupId= :userGroupId"
        )
})
@Getter
@Setter
@NoArgsConstructor
public class UserGroupMemberEntity extends AbstractEntity {

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
    @JoinColumn(name = "userGroupId", nullable = false)
    private UserGroupEntity userGroup;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public UserGroupMemberEntity(final UserGroupEntity userGroupEntity, final User user) {
        this.userGroup = userGroupEntity;
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

        if (!(obj instanceof UserGroupMemberEntity)) {
            return false;
        }

        final UserGroupMemberEntity userGroupMemberEntity = (UserGroupMemberEntity) obj;
        return userGroupMemberEntity.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%d: %s ( %s )", getId(), userGroup, user);
    }
}
