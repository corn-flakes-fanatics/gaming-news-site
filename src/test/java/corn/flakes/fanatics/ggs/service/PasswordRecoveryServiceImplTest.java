package corn.flakes.fanatics.ggs.service;

import corn.flakes.fanatics.ggs.MessageCodeProvider;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.repository.PasswordRecoveryTokenRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class PasswordRecoveryServiceImplTest {
    
    private UserRepository userRepository;
    
    private PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    
    private EmailService emailService;
    
    private PasswordRecoveryServiceImpl recoveryService;
    
    @BeforeEach
    public void setUp() {
        MessageCodeProvider.setMessageSource();
        userRepository = mock(UserRepository.class);
        passwordRecoveryTokenRepository = mock(PasswordRecoveryTokenRepository.class);
        emailService = mock(EmailService.class);
        recoveryService = new PasswordRecoveryServiceImpl(userRepository, passwordRecoveryTokenRepository, emailService);
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
    
    @AfterEach
    public void tearDown() {
        userRepository = null;
        passwordRecoveryTokenRepository = null;
        emailService = null;
        recoveryService = null;
    }
    
}