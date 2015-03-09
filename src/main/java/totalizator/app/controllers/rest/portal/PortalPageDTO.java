package totalizator.app.controllers.rest.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.CupDTO;

import java.util.List;

@JsonIgnoreProperties( ignoreUnknown = true )
public class PortalPageDTO {

	private int id = 0;

	private int userId;
	private String userName;

	private List<CupDTO> cupsShowTo;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId( final int userId ) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public List<CupDTO> getCupsShowTo() {
		return cupsShowTo;
	}

	public void setCupsShowTo( final List<CupDTO> cupsShowTo ) {
		this.cupsShowTo = cupsShowTo;
	}
}
