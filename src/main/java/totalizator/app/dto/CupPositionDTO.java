package totalizator.app.dto;

public class CupPositionDTO {

	private int cupPositionId;
	private String cupPositionName;

	public CupPositionDTO() {
	}

	public CupPositionDTO( final int cupPositionId, final String cupPositionName ) {
		this.cupPositionId = cupPositionId;
		this.cupPositionName = cupPositionName;
	}

	public int getCupPositionId() {
		return cupPositionId;
	}

	public void setCupPositionId( final int cupPositionId ) {
		this.cupPositionId = cupPositionId;
	}

	public String getCupPositionName() {
		return cupPositionName;
	}

	public void setCupPositionName( final String cupPositionName ) {
		this.cupPositionName = cupPositionName;
	}
}
