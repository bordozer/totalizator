package betmen.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static betmen.core.entity.UserGroupEntity.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "userGroups")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select g from UserGroupEntity g order by groupName"
        )
})
@Getter
@Setter
public class UserGroupEntity extends AbstractEntity {

    public static final String LOAD_ALL = "userGroup.loadAll";
    public static final String LOAD_ALL_WHERE_USER_IS_OWNER = "userGroup.loadAllWhereUserIsOwner";
    public static final String LOAD_FOR_CUP_WHERE_USER_IS_OWNER = "SELECT g.* FROM userGroups g JOIN userGroupCups gc ON (g.id = gc.userGroupId ) WHERE g.userId = :userId AND gc.cupId = :cupId ORDER BY g.groupName";

    @Column(length = 100, nullable = false)
    private String groupName;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    private User owner;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup", orphanRemoval = true)
    private List<UserGroupCupEntity> userGroupCups = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userGroup", orphanRemoval = true)
    private List<UserGroupMemberEntity> userGroupMembers = new ArrayList<>();

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof UserGroupEntity)) {
            return false;
        }

        final UserGroupEntity group = (UserGroupEntity) obj;
        return group.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%d: %s ( %s )", getId(), groupName, owner);
    }
}
