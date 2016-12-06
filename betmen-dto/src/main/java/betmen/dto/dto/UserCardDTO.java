package betmen.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserCardDTO {
    private List<CupItemDTO> cupsToShow;
}
