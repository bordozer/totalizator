package betmen.dto.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
@ToString
public class UserCardParametersDTO {
    @NotBlank(message = "errors.user_card_date_should_be_provided")
    private String onDate;
}
