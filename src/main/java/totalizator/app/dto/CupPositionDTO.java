package totalizator.app.dto;

public class CupPositionDTO {

	private int id;
	private String cupPositionName;

	public CupPositionDTO() {
	}

	public CupPositionDTO( final int id, final String cupPositionName ) {
		this.id = id;
		this.cupPositionName = cupPositionName;
	}

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public String getCupPositionName() {
		return cupPositionName;
	}

	public void setCupPositionName( final String cupPositionName ) {
		this.cupPositionName = cupPositionName;
	}
}
