package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.dto.PasswordRecoveryDTO;
import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.service.PasswordRecoveryService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;

import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.CREATE_TOKEN_MAPPING;
import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.PASSWORD_RECOVERY_MAPPING;

@RestController
@RequiredArgsConstructor
public class PasswordRecoveryRestController {
    
    private final PasswordRecoveryService passwordRecoveryService;
    
    @PostMapping(value = CREATE_TOKEN_MAPPING)
    public ResponseMessage<?> generatePasswordRecoveryToken(@PathVariable final String email) throws CannotSendEmailException, AddressException {
        passwordRecoveryService.generateToken(email);
        return new ResponseMessage<>(MessageCode.EMAIL_SENT.getMessage("token"));
    }
    
    @PatchMapping(value = PASSWORD_RECOVERY_MAPPING)
    public ResponseMessage<UserModel> resetPassword(@RequestBody PasswordRecoveryDTO passwordRecoveryDTO) {
        return new ResponseMessage<>(MessageCode.PASSWORD_RESET.getMessage(), passwordRecoveryService.resetPassword(passwordRecoveryDTO));
    }
    
}
