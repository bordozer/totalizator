package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.model.MatchMessage;
import betmen.core.service.MatchMessageService;
import betmen.core.service.UserService;
import betmen.dto.dto.MatchMessageDTO;
import betmen.dto.dto.UserDTO;
import betmen.web.converters.DTOService;
import betmen.web.converters.MatchMessageDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/messages")
public class MatchMessageController {

    @Autowired
    private MatchMessageService matchMessageService;
    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/matches/{matchId}/")
    public List<MatchMessageDTO> findAllByMatchId(final @PathVariable("matchId") int matchId) {
        return bulkConvert(matchMessageService.findAllByMatchId(matchId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}/")
    public List<MatchMessageDTO> findAllByUserId(final @PathVariable("userId") int matchId) {
        return bulkConvert(matchMessageService.findAllByUserId(matchId));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/matches/0")
    public MatchMessageDTO addMatchMessage(@Validated @RequestBody final MatchMessageDTO dto, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final UserDTO userDTO = dtoService.transformUser(currentUser);
        return MatchMessageDtoConverter.convertToDto(matchMessageService.create(MatchMessageDtoConverter.convertToModel(dto), currentUser), userDTO);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/matches/{messageId}")
    public MatchMessageDTO updateMatchMessage(@Validated @RequestBody final MatchMessageDTO dto, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final MatchMessage updatedMessage = matchMessageService.update(MatchMessageDtoConverter.convertToModel(dto), currentUser);
        final UserDTO userDTO = dtoService.transformUser(userService.load(updatedMessage.getId()));
        return MatchMessageDtoConverter.convertToDto(updatedMessage, userDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/matches/{messageId}")
    public boolean deleteMatchMessage(final @PathVariable("messageId") int messageId, final Principal principal) {
        matchMessageService.delete(messageId, getCurrentUser(principal));
        return true;
    }

    private List<MatchMessageDTO> bulkConvert(List<MatchMessage> matchMessages) {
        return matchMessages.stream()
                .map(message -> MatchMessageDtoConverter.convertToDto(message, dtoService.transformUser(userService.load(message.getUserId()))))
                .collect(Collectors.toList());
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}
