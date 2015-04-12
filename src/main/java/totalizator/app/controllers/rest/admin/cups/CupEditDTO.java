package totalizator.app.controllers.rest.admin.cups;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;

public class CupEditDTO {

	private int cupId;
	private String cupName;
	private int categoryId;
	private int winnersCount;

	private LocalDateTime cupStartDate;

	private boolean readyForCupBets;
	private boolean readyForMatchBets;
	private boolean cupBettingIsAllowed;

	private boolean finished;

	private boolean showOnPortalPage;

	private String logoUrl;

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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public int getWinnersCount() {
		return winnersCount;
	}

	public void setWinnersCount( final int winnersCount ) {
		this.winnersCount = winnersCount;
	}

	@JsonSerialize( using = DateTimeSerializer.class )
	public LocalDateTime getCupStartDate() {
		return cupStartDate;
	}

	@JsonDeserialize( using = DateTimeDeserializer.class )
	public void setCupStartDate( final LocalDateTime cupStartDate ) {
		this.cupStartDate = cupStartDate;
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

	public boolean isCupBettingIsAllowed() {
		return cupBettingIsAllowed;
	}

	public void setCupBettingIsAllowed( final boolean cupBettingIsAllowed ) {
		this.cupBettingIsAllowed = cupBettingIsAllowed;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
	}

	public boolean isShowOnPortalPage() {
		return showOnPortalPage;
	}

	public void setShowOnPortalPage( final boolean showOnPortalPage ) {
		this.showOnPortalPage = showOnPortalPage;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl( final String logoUrl ) {
		this.logoUrl = logoUrl;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s", cupId, cupName );
	}
}
