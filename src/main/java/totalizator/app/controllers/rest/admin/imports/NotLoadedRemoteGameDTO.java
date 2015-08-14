package totalizator.app.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class NotLoadedRemoteGameDTO {

	private String remoteGameId;

	public NotLoadedRemoteGameDTO() {
	}

	public NotLoadedRemoteGameDTO( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}

	public String getRemoteGameId() {
		return remoteGameId;
	}

	public void setRemoteGameId( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}

	public boolean isLoaded() {
		return false;
	}

	/*@JsonIgnore
	public void setLoaded() {
	}*/

	@Override
	public String toString() {
		return String.format( "#%s", remoteGameId );
	}
}
