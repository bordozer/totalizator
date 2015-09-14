package totalizator.app.dto;

public class SportKindDTO {

	private int sportKindId;
	private String sportKindName;

	public SportKindDTO( final int sportKindId, final String sportKindName ) {
		this.sportKindId = sportKindId;
		this.sportKindName = sportKindName;
	}

	public int getSportKindId() {
		return sportKindId;
	}

	public void setSportKindId( final int sportKindId ) {
		this.sportKindId = sportKindId;
	}

	public String getSportKindName() {
		return sportKindName;
	}

	public void setSportKindName( final String sportKindName ) {
		this.sportKindName = sportKindName;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", sportKindId, sportKindName );
	}
}
