package betmen.web.converters;

import betmen.core.model.MatchMessage;
import betmen.dto.dto.MatchMessageDTO;
import betmen.dto.dto.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchMessageDtoConverter {

    public static MatchMessageDTO convertToDto(final MatchMessage model, UserDTO user) {
        Assert.notNull(model, "Message should not be null");

        MatchMessageDTO dto = new MatchMessageDTO();
        dto.setId(model.getId());
        dto.setMatchId(model.getMatchId());
        dto.setUser(user);
        dto.setMessageTime(model.getMessageTime());
        dto.setMessageText(model.getMessageText());
        return dto;
    }

    public static MatchMessage convertToModel(final MatchMessageDTO dto) {
        Assert.notNull(dto, "Message DTO should not be null");

        MatchMessage model = new MatchMessage();
        model.setId(dto.getId());
        model.setMatchId(dto.getMatchId());
        model.setMessageText(dto.getMessageText());
        return model;
    }
}
