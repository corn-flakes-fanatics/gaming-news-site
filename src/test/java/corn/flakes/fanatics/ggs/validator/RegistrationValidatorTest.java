package corn.flakes.fanatics.ggs.validator;

import corn.flakes.fanatics.ggs.MessageCodeProvider;
import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.messages.ValidationMessageContainer;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

public class RegistrationValidatorTest {
    
    private UserRepository userRepository;
    
    private RegistrationValidator registrationValidator;
    
    @BeforeEach
    public void setUp() {
        MessageCodeProvider.setMessageSource();
        userRepository = mock(UserRepository.class);
        registrationValidator = new RegistrationValidator(userRepository);
    }
    
    @Test
    public void shouldHaveErrorsWithNullObject() {
        final ValidationMessageContainer result = registrationValidator.validate(null);
        
        assertThat(result.getMessages(), hasItem("Object cannot be empty"));
        assertThat(result.getMessages(), hasSize(1));
        assertThat(result.hasMessages(), is(true));
    }
    
    @Test
    public void shouldHaveErrorsWithNullFields() {
        final ValidationMessageContainer result = registrationValidator.validate(new RegistrationDTO());
        
        assertEmptyFields(result);
    }
    
    @Test
    public void shouldHaveErrorsWithEmptyFields() {
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("   ")
                .username("")
                .email("   ")
                .password("        ")
                .build();
        
        final ValidationMessageContainer result = registrationValidator.validate(registrationDTO);
        
        assertEmptyFields(result);
    }
    
    @Test
    public void shouldHaveErrorsWithInvalidEmailFormat() {
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("asdasd")
                .username("asdasd")
                .email("sdfgsdfsdf@@@awqeasd")
                .password("zaq1@WSX")
                .build();
        
        final ValidationMessageContainer result = registrationValidator.validate(registrationDTO);
        
        assertThat(result.getMessages(), hasItem("Provided email is invalid"));
        assertThat(result.getMessages(), hasSize(1));
        assertThat(result.hasMessages(), is(true));
    }
    
    @Test
    public void shouldHaveErrorsWithNotStrongPassword() {
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("asdasd")
                .username("asdasd")
                .email("sdfgsdfsdf@awqeasd.com")
                .password("sdfgsdfZAAS23")
                .build();
        
        final ValidationMessageContainer result = registrationValidator.validate(registrationDTO);
        
        assertThat(result.getMessages(), hasItem("Provided password is not strong enough"));
        assertThat(result.getMessages(), hasSize(1));
        assertThat(result.hasMessages(), is(true));
    }
    
    @Test
    public void shouldHaveErrorsWithDuplicatedLoginUsernameAndEmail() {
        when(userRepository.findByLogin(anyString())).thenReturn(new UserModel());
        when(userRepository.findByUsername(anyString())).thenReturn(new UserModel());
        when(userRepository.findByEmail(anyString())).thenReturn(new UserModel());
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("asdasd")
                .username("asdasd")
                .email("sdfgsdfsdf@awqeasd.com")
                .password("zaq1@WSX")
                .build();
        
        final ValidationMessageContainer result = registrationValidator.validate(registrationDTO);
        
        assertThat(result.getMessages(), hasItems("Login is already taken", "Username is already taken", "Email is already taken"));
        assertThat(result.getMessages(), hasSize(3));
        assertThat(result.hasMessages(), is(true));
    }
    
    @Test
    public void shouldNotHaveErrorsWithValidObject() {
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("asdasd")
                .username("asdasd")
                .email("sdfgsdfsdf@awqeasd.com")
                .password("zaq1@WSX")
                .build();
        
        final ValidationMessageContainer result = registrationValidator.validate(registrationDTO);
        
        assertThat(result.hasMessages(), is(false));
    }
    
    @AfterEach
    public void tearDown() {
        userRepository = null;
        registrationValidator = null;
    }
    
    private void assertEmptyFields(ValidationMessageContainer result) {
        assertThat(result.getMessages(),
                hasItems("Login cannot be empty", "Username cannot be empty", "Email cannot be empty", "Password cannot be empty"));
        assertThat(result.getMessages(), hasSize(4));
        assertThat(result.hasMessages(), is(true));
    }
    
}