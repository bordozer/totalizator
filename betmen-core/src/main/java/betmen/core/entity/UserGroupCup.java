package betmen.core.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.UserGroupCup.LOAD_ALL_USER_GROUP_CUPS;
import static betmen.core.entity.UserGroupCup.LOAD_CUPS_FOR_USER_GROUP;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "userGroupCups")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL_USER_GROUP_CUPS,
                query = "select g from UserGroupCup g"
        ),
        @NamedQuery(
                name = LOAD_CUPS_FOR_USER_GROUP,
                query = "select g from UserGroupCup g where userGroupId= :userGroupId"
        )
})
public class UserGroupCup extends AbstractEntity {

    public static final String LOAD_ALL_USER_GROUP_CUPS = "userGroupCup.loadAllUserGroupCups";
    public static final String LOAD_CUPS_FOR_USER_GROUP = "userGroupCup.loadCupsForUserGroups";

    @ManyToOne
    @JoinColumn(name = "userGroupId")
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "cupId")
    private Cup cup;

    public UserGroupCup() {
    }

    public UserGroupCup(final UserGroup userGroup, final Cup cup) {
        this.userGroup = userGroup;
        this.cup = cup;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Cup getCup() {
        return cup;
    }

    public void setCup(final Cup cup) {
        this.cup = cup;
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

        if (!(obj instanceof UserGroupCup)) {
            return false;
        }

        final UserGroupCup userGroupCup = (UserGroupCup) obj;
        return userGroupCup.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("#%d: %s ( %s )", getId(), userGroup, cup);
    }
}
