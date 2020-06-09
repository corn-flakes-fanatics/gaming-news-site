package corn.flakes.fanatics.ggs.service;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;

import javax.mail.internet.AddressException;

public interface EmailService {
    
    /**
     * Send email message with token used for password recovery to provided address
     *
     * @param token generated password recovery token
     * @param email user's email who requested password recovery
     * @return created email which has been sent (for testing purposes)
     * @throws CannotSendEmailException if email cannot be sent (e.g. smtp server is not working)
     * @throws AddressException         if given address is not valid and cannot be parsed
     * @see corn.flakes.fanatics.ggs.service.EmailConstants
     */
    Email sendPasswordRecoveryEmail(String token, String email) throws CannotSendEmailException, AddressException;
    
}
