package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static corn.flakes.fanatics.ggs.service.RegistrationService.*;
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
    
    @GetMapping(value = CHECK_LOGIN_MAPPING)
    public ResponseMessage<String> checkLogin(@PathVariable String login) {
        final boolean loginExists = registrationService.loginExists(login);
        return new ResponseMessage<>(String.valueOf(loginExists));
    }
    
    @GetMapping(value = CHECK_USERNAME_MAPPING)
    public ResponseMessage<String> checkUsername(@PathVariable String username) {
        final boolean usernameExists = registrationService.usernameExists(username);
        return new ResponseMessage<>(String.valueOf(usernameExists));
    }
    
    @GetMapping(value = CHECK_EMAIL_MAPPING)
    public ResponseMessage<String> checkEmail(@PathVariable String email) {
        final boolean emailExists = registrationService.emailExists(email);
        return new ResponseMessage<>(String.valueOf(emailExists));
    }
    
}
