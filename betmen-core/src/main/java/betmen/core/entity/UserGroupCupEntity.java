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

import static betmen.core.entity.UserGroupCupEntity.LOAD_ALL_USER_GROUP_CUPS;
import static betmen.core.entity.UserGroupCupEntity.LOAD_CUPS_FOR_USER_GROUP;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "userGroupCups")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL_USER_GROUP_CUPS,
                query = "select g from UserGroupCupEntity g"
        ),
        @NamedQuery(
                name = LOAD_CUPS_FOR_USER_GROUP,
                query = "select g from UserGroupCupEntity g where userGroupId= :userGroupId"
        )
})
@Getter
@Setter
@NoArgsConstructor
public class UserGroupCupEntity extends AbstractEntity {

    public static final String LOAD_ALL_USER_GROUP_CUPS = "userGroupCup.loadAllUserGroupCups";
    public static final String LOAD_CUPS_FOR_USER_GROUP = "userGroupCup.loadCupsForUserGroups";
    public static final String LOAD_USER_GROUP_CUPS_FOR_CUP = "select ugc.* FROM userGroupCups ugc WHERE ugc.cupId = :cupId";

    @ManyToOne
    @JoinColumn(name = "userGroupId", nullable = false)
    private UserGroupEntity userGroup;

    @OneToOne
    @JoinColumn(name = "cupId", nullable = false)
    private Cup cup;

    public UserGroupCupEntity(final UserGroupEntity userGroupEntity, final Cup cup) {
        this.userGroup = userGroupEntity;
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

        if (!(obj instanceof UserGroupCupEntity)) {
            return false;
        }

        final UserGroupCupEntity userGroupCupEntity = (UserGroupCupEntity) obj;
        return userGroupCupEntity.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("#%d: %s ( %s )", getId(), userGroup, cup);
    }
}
