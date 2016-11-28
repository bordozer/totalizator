package betmen.core.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.FavoriteCategory.LOAD_ALL;
import static betmen.core.entity.FavoriteCategory.LOAD_FOR_USER;
import static betmen.core.entity.FavoriteCategory.LOAD_FOR_USER_AND_CATEGORY;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "favorites")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from FavoriteCategory c"
        ),
        @NamedQuery(
                name = LOAD_FOR_USER,
                query = "select c from FavoriteCategory c where userId= :userId"
        ),
        @NamedQuery(
                name = LOAD_FOR_USER_AND_CATEGORY,
                query = "select c from FavoriteCategory c where userId= :userId and categoryId= :categoryId"
        )
})
public class FavoriteCategory extends AbstractEntity {

    public static final String LOAD_ALL = "favorites.loadAll";
    public static final String LOAD_FOR_USER = "favorites.loadForUser";
    public static final String LOAD_FOR_USER_AND_CATEGORY = "favorites.loadForUserAndCategory";

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false, updatable = false)
    private Category category;

    public FavoriteCategory() {
    }

    public FavoriteCategory(final User user, final Category category) {
        this.user = user;
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    @Override
    public boolean equals(final Object o) {

        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        final FavoriteCategory favoriteCategory = (FavoriteCategory) o;

        return favoriteCategory.getId() == getId();
    }

    @Override
    public String toString() {
        return String.format("%s: %s", user, category);
    }
}
