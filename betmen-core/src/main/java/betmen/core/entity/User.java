package betmen.core.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static betmen.core.entity.User.LOAD_ALL;

@Entity
@org.hibernate.annotations.Cache(region = "common", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "users")
@NamedQueries({
        @NamedQuery(
                name = LOAD_ALL,
                query = "select c from User c order by username"
        ),
        @NamedQuery(
                name = User.FIND_BY_USERNAME,
                query = "select u from User u where username = :username"
        ),
        @NamedQuery(
                name = User.FIND_BY_LOGIN,
                query = "select u from User u where login = :login"
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends AbstractEntity {

    public static final String LOAD_ALL = "user.loadAll";
    public static final String FIND_BY_USERNAME = "user.findByUserName";
    public static final java.lang.String FIND_BY_LOGIN = "user.findByUserLogin";

    @Column(unique = true, length = 20)
    private String login;

    @Column(unique = true, length = 200)
    private String username;

    @Column(length = 255)
    private String password;
}
