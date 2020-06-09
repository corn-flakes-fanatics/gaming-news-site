package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.AddressException;

public interface PasswordRecoveryService {
    
    String CREATE_TOKEN_MAPPING = "/passwordRecovery/{email}";
    
    /**
     * Generate new password recovery token for user
     *
     * @param email user's email address
     * @return generated password recovery token
     * @throws UserNotFoundException in case if user's email was not found
     */
    PasswordRecoveryToken generateToken(String email) throws UserNotFoundException, AddressException, CannotSendEmailException;
    
}
