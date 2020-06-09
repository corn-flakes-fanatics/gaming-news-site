package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.service.PasswordRecoveryService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Date;

import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.CREATE_TOKEN_MAPPING;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

public class PasswordRecoveryRestControllerTest extends RestControllerAbstractTest {
    
    @MockBean
    private PasswordRecoveryService passwordRecoveryService;
    
    @Test
    public void shouldReturnStatus404IfUserWasNotFound() throws Exception {
        final String mapping = CREATE_TOKEN_MAPPING.replace("{email}", "asdf@gmail.com");
        when(passwordRecoveryService.generateToken(anyString())).thenThrow(new UserNotFoundException("Email was not found"));
        
        mockMvc.perform(post(mapping))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Email was not found")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldReturnStatus500IfEmailCannotBeSent() throws Exception {
        final String mapping = CREATE_TOKEN_MAPPING.replace("{email}", "asdf@gmail.com");
        when(passwordRecoveryService.generateToken(anyString())).thenThrow(new CannotSendEmailException());
        
        mockMvc.perform(post(mapping))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Server side error occurred. Please try again")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isInternalServerError());
    }
    
    @Test
    public void shouldReturnStatus200IfUserWasFound() throws Exception {
        final UserModel user = UserModel.builder()
                .email("asdf@gmail.com")
                .build();
        final PasswordRecoveryToken token = PasswordRecoveryToken.builder()
                .id("asd123123aqsd")
                .token("45rsdf-asdf123-asdasd-zxc")
                .user(new UserModel())
                .expirationDate(new Date(new Date().getTime() + 2000L))
                .build();
        final String mapping = CREATE_TOKEN_MAPPING.replace("{email}", user.getEmail());
        when(passwordRecoveryService.generateToken(user.getEmail())).thenReturn(token);
        
        mockMvc.perform(post(mapping))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Email with token has been sent")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}