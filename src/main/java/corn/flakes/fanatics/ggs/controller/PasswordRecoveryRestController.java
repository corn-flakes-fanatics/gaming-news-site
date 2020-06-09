package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.service.PasswordRecoveryService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.AddressException;

import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.CREATE_TOKEN_MAPPING;

@RestController
@RequiredArgsConstructor
public class PasswordRecoveryRestController {
    
    private final PasswordRecoveryService passwordRecoveryService;
    
    @PostMapping(value = CREATE_TOKEN_MAPPING, produces = "application/json")
    public ResponseMessage<?> generatePasswordRecoveryToken(@PathVariable final String email) throws CannotSendEmailException, AddressException {
        passwordRecoveryService.generateToken(email);
        return new ResponseMessage<>(MessageCode.EMAIL_SENT.getMessage("token"));
    }
    
}
