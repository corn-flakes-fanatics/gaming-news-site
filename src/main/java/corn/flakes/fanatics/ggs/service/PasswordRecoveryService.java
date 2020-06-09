package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.dto.PasswordRecoveryDTO;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.TokenNotFoundException;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.AddressException;

public interface PasswordRecoveryService {
    
    String PASSWORD_RECOVERY_MAPPING = "/passwordRecovery";
    
    String CREATE_TOKEN_MAPPING = PASSWORD_RECOVERY_MAPPING + "/{email}";
    
    /**
     * Generate new password recovery token for user
     *
     * @param email user's email address
     * @return generated password recovery token
     * @throws UserNotFoundException in case if user's email was not found
     */
    PasswordRecoveryToken generateToken(String email) throws UserNotFoundException, AddressException, CannotSendEmailException;
    
    /**
     * Reset user's password
     *
     * @param passwordRecoveryDTO dto containing token and new password
     * @return user with new hashed password
     * @throws TokenNotFoundException if token was not found
     * @throws ValidationException    if token is expired or new password is weak
     */
    UserModel resetPassword(PasswordRecoveryDTO passwordRecoveryDTO) throws TokenNotFoundException, ValidationException;
    
}
