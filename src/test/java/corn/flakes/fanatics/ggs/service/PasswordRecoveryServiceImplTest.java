package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.MessageCodeProvider;
import corn.flakes.fanatics.ggs.dto.PasswordRecoveryDTO;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.TokenNotFoundException;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.repository.PasswordRecoveryTokenRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class PasswordRecoveryServiceImplTest {
    
    private UserRepository userRepository;
    
    private PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    private EmailService emailService;
    
    private PasswordEncoder passwordEncoder;
    
    private PasswordRecoveryService recoveryService;
    
    @BeforeEach
    public void setUp() {
        MessageCodeProvider.setMessageSource();
        userRepository = mock(UserRepository.class);
        passwordRecoveryTokenRepository = mock(PasswordRecoveryTokenRepository.class);
        emailService = mock(EmailService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        recoveryService = new PasswordRecoveryServiceImpl(userRepository, passwordRecoveryTokenRepository, emailService, passwordEncoder);
    }
    
    @Test
    public void shouldThrowExceptionIfUserEmailWasNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        
        final UserNotFoundException result = assertThrows(UserNotFoundException.class, () -> recoveryService.generateToken("asdasd"));
        
        assertThat(result.getMessage(), is("Email was not found"));
    }
    
    @Test
    public void shouldCreateNewTokenAndRemoveOldToken() throws Exception {
        final PasswordRecoveryToken oldToken = PasswordRecoveryToken.builder()
                .id("asd2134")
                .build();
        final UserModel user = UserModel.builder()
                .id("123sadasdf")
                .email("sad@gmail.com")
                .build();
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordRecoveryTokenRepository.findByUser(any(UserModel.class))).thenReturn(oldToken);
        when(passwordRecoveryTokenRepository.save(any(PasswordRecoveryToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        final PasswordRecoveryToken result = recoveryService.generateToken("asd");
        
        assertThat(result, is(notNullValue()));
        assertThat(result.getExpirationDate()
                .after(new Date()), is(true));
        
        verify(passwordRecoveryTokenRepository, times(1)).deleteById(oldToken.getId());
        verify(emailService, times(1)).sendPasswordRecoveryEmail(result.getToken(), user.getEmail());
    }
    
    @Test
    public void shouldThrowExceptionIfTokenWasNotFound() {
        final String token = "asd";
        when(passwordRecoveryTokenRepository.findByToken(anyString())).thenReturn(null);
        
        final TokenNotFoundException result =
                assertThrows(TokenNotFoundException.class, () -> recoveryService.resetPassword(PasswordRecoveryDTO.builder()
                        .token(token)
                        .build()));
        
        assertThat(result.getMessage(), is("Token " + token + " was not found"));
    }
    
    @Test
    public void shouldThrowExceptionIfTokenHasExpired() {
        final PasswordRecoveryToken token = PasswordRecoveryToken.builder()
                .expirationDate(new Date(new Date().getTime() - 6000L))
                .token("asd")
                .build();
        final PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO("asd", "asd");
        when(passwordRecoveryTokenRepository.findByToken(anyString())).thenReturn(token);
        
        final ValidationException result = assertThrows(ValidationException.class, () -> recoveryService.resetPassword(passwordRecoveryDTO));
        
        assertThat(result.getMessages(), hasItem("Token has expired"));
    }
    
    @Test
    public void shouldChangePasswordAndDeleteToken() {
        final String password = "zaq1@WSX";
        final PasswordRecoveryToken token = PasswordRecoveryToken.builder()
                .id("q23sdasd")
                .expirationDate(new Date(new Date().getTime() + 6000L))
                .user(UserModel.builder()
                        .password(password)
                        .build())
                .token("asd")
                .build();
        final PasswordRecoveryDTO passwordRecoveryDTO = new PasswordRecoveryDTO("asd", "zaq1@WSX");
        when(passwordRecoveryTokenRepository.findByToken(anyString())).thenReturn(token);
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        recoveryService.resetPassword(passwordRecoveryDTO);
        
        verify(passwordEncoder, times(1)).encode(password);
        verify(passwordRecoveryTokenRepository, times(1)).deleteById(token.getId());
    }
    
    @AfterEach
    public void tearDown() {
        userRepository = null;
        passwordRecoveryTokenRepository = null;
        emailService = null;
        passwordEncoder = null;
        recoveryService = null;
    }
    
}