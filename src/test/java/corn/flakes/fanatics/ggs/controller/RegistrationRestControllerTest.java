package corn.flakes.fanatics.ggs.controller;

import com.google.common.collect.Lists;
import corn.flakes.fanatics.ggs.dto.RegistrationDTO;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.service.RegistrationService;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static corn.flakes.fanatics.ggs.service.RegistrationService.REGISTER_MAPPING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RegistrationRestControllerTest extends RestControllerAbstractTest {
    
    @MockBean
    private RegistrationService registrationService;
    
    @Test
    public void shouldReturnStatus400IfValidationDidNotPass() throws Exception {
        final String message1 = "asd";
        final String message2 = "dsa";
        final String dtoAsJson = objectMapper.writeValueAsString(new RegistrationDTO());
        when(registrationService.registerUser(any(RegistrationDTO.class))).thenThrow(new ValidationException(Lists.newArrayList(message1, message2)));
        
        mockMvc.perform(post(REGISTER_MAPPING).contentType(MediaType.APPLICATION_JSON)
                .content(dtoAsJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Validation did not pass")))
                .andExpect(jsonPath("$.object[0]", is(message1)))
                .andExpect(jsonPath("$.object[1]", is(message2)))
                .andExpect(redirectedUrl(null))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void shouldReturnStatus201WithSavedUser() throws Exception {
        final UserModel userModel = UserModel.builder()
                .id("ASD2353425&%sad")
                .login("asdf")
                .username("test")
                .password("zxc")
                .email("asd@asd.com")
                .build();
        final String dtoAsJson = objectMapper.writeValueAsString(new RegistrationDTO());
        when(registrationService.registerUser(any(RegistrationDTO.class))).thenReturn(userModel);
        
        mockMvc.perform(post(REGISTER_MAPPING).contentType(MediaType.APPLICATION_JSON)
                .content(dtoAsJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("You have been registered")))
                .andExpect(jsonPath("$.object.id").doesNotExist())
                .andExpect(jsonPath("$.object.password").doesNotExist())
                .andExpect(jsonPath("$.object.login", is(userModel.getLogin())))
                .andExpect(jsonPath("$.object.username", is(userModel.getUsername())))
                .andExpect(jsonPath("$.object.email", is(userModel.getEmail())))
                .andExpect(redirectedUrlPattern("**/register/{id}"))
                .andExpect(forwardedUrl(null))
                .andExpect(status().isCreated());
    }
    
}