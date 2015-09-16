package totalizator.app.controllers.rest.admin.cups.edit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateTimeDeserializer;
import totalizator.app.dto.serialization.DateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties( value = {
	"cupBetAllowance"
} )
public class CupEditDTO {

	private int cupId;
	private String cupName;
	private int categoryId;
	private int winnersCount;

	private LocalDateTime cupStartDate;

	private boolean readyForCupBets;
	private boolean readyForMatchBets;

	private boolean finished;

	private boolean publicCup;

	private String logoUrl;

	private List<CupWinnerEditDTO> cupWinners;

	private int cupPointsCalculationStrategyId;

	private String cupImportId;

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

	public boolean isFinished() {
		return finished;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
	}

	public boolean isPublicCup() {
		return publicCup;
	}

	public void setPublicCup( final boolean publicCup ) {
		this.publicCup = publicCup;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl( final String logoUrl ) {
		this.logoUrl = logoUrl;
	}

	public List<CupWinnerEditDTO> getCupWinners() {
		return cupWinners;
	}

	public void setCupWinners( final List<CupWinnerEditDTO> cupWinners ) {
		this.cupWinners = cupWinners;
	}

	public int getCupPointsCalculationStrategyId() {
		return cupPointsCalculationStrategyId;
	}

	public void setCupPointsCalculationStrategyId( final int cupPointsCalculationStrategyId ) {
		this.cupPointsCalculationStrategyId = cupPointsCalculationStrategyId;
	}

	public String getCupImportId() {
		return cupImportId;
	}

	public void setCupImportId( final String cupImportId ) {
		this.cupImportId = cupImportId;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s", cupId, cupName );
	}
}
