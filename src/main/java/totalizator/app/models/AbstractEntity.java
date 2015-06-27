package totalizator.app.models;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
public class AbstractEntity {

	@Id
	@GeneratedValue
	private int id;

	@Version
	private Long version;

	@Transient
	private UUID uuid;

	@Column(name = "UUID")
	private String uuidStr;

	@PrePersist
	protected void prePersist() {
		syncUuidString();
	}

	protected void syncUuidString() {
		if ( null == uuidStr ) {
			getUuid();
		}
	}

	public UUID getUuid() {
		if ( uuidStr == null ) {
			if ( uuid == null ) {
				uuid = UUID.randomUUID();
			}
			uuidStr = uuid.toString();
		}
		if ( uuid == null && uuidStr != null ) {
			uuid = UUID.fromString( uuidStr );
		}
		return uuid;
	}

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;

		AbstractEntity that = ( AbstractEntity ) o;

		if ( getUuid() != null ? !getUuid().equals( that.getUuid() ) : that.getUuid() != null ) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return getUuid() != null ? getUuid().hashCode() : 0;
	}

	public Long getVersion() {
		return version;
	}

	public String getUuidStr() {
		return uuidStr;
	}

	@Override
	public String toString() {
		return String.format( "#%d", id );
	}
}
