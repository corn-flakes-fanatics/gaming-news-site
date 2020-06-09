package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.repository.PasswordRecoveryTokenRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    
    private final UserRepository userRepository;
    
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    private final EmailService emailService;
    
    @Override
    public PasswordRecoveryToken generateToken(String email) throws UserNotFoundException, AddressException, CannotSendEmailException {
        final UserModel user = userRepository.findByEmail(email);
        if ( user == null ) {
            throw new UserNotFoundException(MessageCode.EMAIL_NOT_FOUND.getMessage());
        }
        removeOldTokens(user);
        final PasswordRecoveryToken token = PasswordRecoveryToken.builder()
                .token(generateTokenValue())
                .user(user)
                .expirationDate(createExpirationDate())
                .build();
        
        emailService.sendPasswordRecoveryEmail(token.getToken(), user.getEmail());
        return passwordRecoveryTokenRepository.save(token);
    }
    
    private void removeOldTokens(UserModel user) {
        final PasswordRecoveryToken oldToken = passwordRecoveryTokenRepository.findByUser(user);
        if ( oldToken != null ) {
            log.debug("Removing old token with id: {} for user with email: {}", oldToken.getId(), user.getEmail());
            passwordRecoveryTokenRepository.deleteById(oldToken.getId());
        }
    }
    
    private String generateTokenValue() {
        String token;
        do {
            token = UUID.randomUUID()
                    .toString();
        } while ( passwordRecoveryTokenRepository.findByToken(token) != null );
        
        return token;
    }
    
    private Date createExpirationDate() {
        final Date now = new Date();
        return new Date(now.getTime() + (1000 * 60 * 60));
    }
    
}
