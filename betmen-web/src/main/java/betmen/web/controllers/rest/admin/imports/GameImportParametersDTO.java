package betmen.web.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GameImportParametersDTO {
    private String dateFrom;
    private String dateTo;
    private int cupId;
}
