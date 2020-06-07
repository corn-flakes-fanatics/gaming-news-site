package corn.flakes.fanatics.ggs.service;

import com.google.common.collect.Lists;
import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.messages.ValidationMessageContainer;
import corn.flakes.fanatics.ggs.model.Role;
import corn.flakes.fanatics.ggs.model.RoleModel;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.repository.RoleRepository;
import corn.flakes.fanatics.ggs.repository.UserRepository;
import corn.flakes.fanatics.ggs.validator.RegistrationValidator;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

public class RegistrationServiceImplTest {
    
    private UserRepository userRepository;
    
    private RoleRepository roleRepository;
    
    private PasswordEncoder passwordEncoder;
    
    private RegistrationValidator registrationValidator;
    
    private RegistrationService registrationService;
    
    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        registrationValidator = mock(RegistrationValidator.class);
        registrationService = new RegistrationServiceImpl(userRepository, roleRepository, passwordEncoder, registrationValidator);
    }
    
    @Test
    public void shouldThrowExceptionIfValidationDidNotPass() {
        final String message1 = "test1";
        final String message2 = "test2";
        when(registrationValidator.validate(any(RegistrationDTO.class))).thenReturn(
                new ValidationMessageContainer(Lists.newArrayList(message1, message2)));
        
        final ValidationException result = assertThrows(ValidationException.class, () -> registrationService.registerUser(new RegistrationDTO()));
        
        assertThat(result.getMessages(), hasItems(message1, message2));
    }
    
    @Test
    public void shouldReturnSavedUserWithHashedPassword() {
        final RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .login("asdasd")
                .username("asdasd")
                .email("sdfgsdfsdf@awqeasd.com")
                .password("zaq1@WSX")
                .build();
        when(registrationValidator.validate(any())).thenReturn(new ValidationMessageContainer());
        when(roleRepository.findByValue(any(Role.class))).thenReturn(new RoleModel());
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        final UserModel result = registrationService.registerUser(registrationDTO);
        
        assertThat(result, is(notNullValue()));
        verify(passwordEncoder, times(1)).encode(registrationDTO.getPassword());
    }
    
    @AfterEach
    public void tearDown() {
        userRepository = null;
        roleRepository = null;
        passwordEncoder = null;
        registrationValidator = null;
        registrationService = null;
    }
    
}