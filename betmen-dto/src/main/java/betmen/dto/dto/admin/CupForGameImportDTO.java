package betmen.dto.dto.admin;

import betmen.dto.dto.CupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CupForGameImportDTO {
    private CupDTO cup;
    private int timePeriodType;
}
