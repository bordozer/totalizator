package totalizator.app.controllers.rest.matches.data;

import totalizator.app.dto.CategoryDTO;
import totalizator.app.dto.CupDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.dto.UserDTO;

import java.util.List;

public class MatchesDataDTO {

	private List<UserDTO> users;
	private List<CategoryDTO> categories;
	private List<CupDTO> cups;
	private List<TeamDTO> teams;

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers( final List<UserDTO> users ) {
		this.users = users;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories( final List<CategoryDTO> categories ) {
		this.categories = categories;
	}

	public List<CupDTO> getCups() {
		return cups;
	}

	public void setCups( final List<CupDTO> cups ) {
		this.cups = cups;
	}

	public List<TeamDTO> getTeams() {
		return teams;
	}

	public void setTeams( final List<TeamDTO> teams ) {
		this.teams = teams;
	}
}
