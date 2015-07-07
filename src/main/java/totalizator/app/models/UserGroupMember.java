package totalizator.app.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import static totalizator.app.models.UserGroupMember.*;

@Entity
@org.hibernate.annotations.Cache( region = "common", usage = CacheConcurrencyStrategy.READ_WRITE )
@Table( name = "userGroupMembers" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL,
				query = "select g from UserGroupMember g order by groupName"
		),
		@NamedQuery(
				name = LOAD_USER_GROUP_MEMBERS,
				query = "select g from UserGroupMember g where userGroupId= :userGroupId"
		),
		@NamedQuery(
				name = LOAD_USER_GROUP_WHERE_USER_IS_MEMBER,
				query = "select g from UserGroupMember g where userId= :userId"
		),
		@NamedQuery(
				name = LOAD_USER_GROUP_MEMBER_ENTRY,
				query = "select g from UserGroupMember g where userId= :userId and userGroupId= :userGroupId"
		)
} )
public class UserGroupMember extends AbstractEntity {

	public static final String LOAD_ALL = "userGroupMembers.loadAll";
	public static final String LOAD_USER_GROUP_MEMBERS = "userGroupMembers.loadMembers";
	public static final String LOAD_USER_GROUP_WHERE_USER_IS_MEMBER = "userGroupMembers.loadWhereUserIsMembers";
	public static final String LOAD_USER_GROUP_MEMBER_ENTRY = "userGroupMembers.loadUserGroupMemberEntry";

	@ManyToOne
	@JoinColumn( name = "userGroupId" )
	private UserGroup userGroup;

	@ManyToOne
	@JoinColumn( name = "userId" )
	private User user;

	public UserGroupMember() {
	}

	public UserGroupMember( final UserGroup userGroup, final User user ) {
		this.userGroup = userGroup;
		this.user = user;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup( final UserGroup userGroup ) {
		this.userGroup = userGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser( final User user ) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return 31 * userGroup.getId();
	}

	@Override
	public boolean equals( final Object obj ) {

		if ( obj == null ) {
			return false;
		}

		if ( obj == this ) {
			return true;
		}

		if ( !( obj instanceof UserGroupMember ) ) {
			return false;
		}

		final UserGroupMember userGroupMember = ( UserGroupMember ) obj;
		return userGroupMember.getId() == getId();
	}

	@Override
	public String toString() {
		return String.format( "%d: %s ( %s )", getId(), userGroup, user );
	}
}
