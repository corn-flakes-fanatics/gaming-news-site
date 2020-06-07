package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static corn.flakes.fanatics.ggs.service.RegistrationService.REGISTER_MAPPING;
import static corn.flakes.fanatics.ggs.utils.UriUtils.createFromId;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegistrationRestController {
    
    private final RegistrationService registrationService;
    
    @PostMapping(value = REGISTER_MAPPING)
    public ResponseEntity<ResponseMessage<UserModel>> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        final UserModel registeredUser = registrationService.registerUser(registrationDTO);
        return ResponseEntity.created(createFromId(registeredUser.getId()))
                .body(new ResponseMessage<>(MessageCode.REGISTERED.getMessage(), registeredUser));
    }
    
}
