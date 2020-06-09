package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.dto.PasswordRecoveryDTO;
import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.TokenNotFoundException;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.repository.PasswordRecoveryTokenRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import corn.flakes.fanatics.ggs.utils.ValidationUtils;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    
    private final UserRepository userRepository;
    
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    private final EmailService emailService;
    
    private final PasswordEncoder passwordEncoder;
    
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
    
    @Override
    public UserModel resetPassword(PasswordRecoveryDTO passwordRecoveryDTO) throws TokenNotFoundException, ValidationException {
        final String tokenFromRequest = passwordRecoveryDTO.getToken();
        final String password = passwordRecoveryDTO.getPassword();
        final PasswordRecoveryToken token = passwordRecoveryTokenRepository.findByToken(tokenFromRequest);
        validateTokenAndPassword(tokenFromRequest, token, password);
        
        UserModel user = token.getUser();
        user.setPassword(passwordEncoder.encode(password));
        passwordRecoveryTokenRepository.deleteById(token.getId());
        return userRepository.save(user);
    }
    
    private void validateTokenAndPassword(String tokenFromRequest, PasswordRecoveryToken token, String password) {
        if ( token == null ) {
            throw new TokenNotFoundException(MessageCode.TOKEN_NOT_FOUND.getMessage(tokenFromRequest));
        } else if ( !isTokenValid(token) ) {
            throw new ValidationException(Collections.singletonList(MessageCode.TOKEN_EXPIRED.getMessage()));
        } else if ( !ValidationUtils.isPasswordValid(password) ) {
            throw new ValidationException(Collections.singletonList(MessageCode.PASSWORD_NOT_STRONG_ENOUGH.getMessage()));
        }
    }
    
    private boolean isTokenValid(PasswordRecoveryToken token) {
        return new Date().before(token.getExpirationDate());
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
