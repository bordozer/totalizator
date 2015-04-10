package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CupDTO {

	private int cupId;
	private String cupName;
	private CategoryDTO category;
	private int winnersCount;
	private boolean showOnPortalPage;

	private Date cupStartDate;

	private boolean readyForCupBets;
	private boolean readyForMatchBets;

	private boolean finished;

	public CupDTO() {
	}

	public CupDTO( final int cupId, final String cupName, final CategoryDTO category ) {
		this.cupId = cupId;
		this.cupName = cupName;
		this.category = category;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory( final CategoryDTO category ) {
		this.category = category;
	}

	public boolean isShowOnPortalPage() {
		return showOnPortalPage;
	}

	public void setShowOnPortalPage( final boolean showOnPortalPage ) {
		this.showOnPortalPage = showOnPortalPage;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public Date getCupStartDate() {
		return cupStartDate;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setCupStartDate( final Date cupStartDate ) {
		this.cupStartDate = cupStartDate;
	}

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	public boolean isReadyForCupBets() {
		return readyForCupBets;
	}

	public void setReadyForCupBets( final boolean readyForCupBets ) {
		this.readyForCupBets = readyForCupBets;
	}

	public boolean isReadyForMatchBets() {
		return readyForMatchBets;
	}

	public void setReadyForMatchBets( final boolean readyForMatchBets ) {
		this.readyForMatchBets = readyForMatchBets;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s", cupId, cupName );
	}
}
