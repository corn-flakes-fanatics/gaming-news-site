package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.dto.PasswordRecoveryDTO;
import corn.flakes.fanatics.ggs.model.PasswordRecoveryToken;
import corn.flakes.fanatics.ggs.model.TokenNotFoundException;
import corn.flakes.fanatics.ggs.model.UserModel;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.service.PasswordRecoveryService;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Date;

import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.CREATE_TOKEN_MAPPING;
import static corn.flakes.fanatics.ggs.service.PasswordRecoveryService.PASSWORD_RECOVERY_MAPPING;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;

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
    
    @Test
    public void shouldReturnStatus404IfTokenWasNotFound() throws Exception {
        final String dtoAsJson = objectMapper.writeValueAsString(new PasswordRecoveryDTO());
        when(passwordRecoveryService.resetPassword(any(PasswordRecoveryDTO.class))).thenThrow(new TokenNotFoundException("Token was not found"));
        
        mockMvc.perform(patch(PASSWORD_RECOVERY_MAPPING).contentType(MediaType.APPLICATION_JSON)
                .content(dtoAsJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Token was not found")))
                .andExpect(jsonPath("$.object", is(nullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void shouldReturnStatus400IfTokenHasExpired() throws Exception {
        final String dtoAsJson = objectMapper.writeValueAsString(new PasswordRecoveryDTO());
        when(passwordRecoveryService.resetPassword(any(PasswordRecoveryDTO.class))).thenThrow(
                new ValidationException(Collections.singletonList("Token has expired")));
        
        mockMvc.perform(patch(PASSWORD_RECOVERY_MAPPING).contentType(MediaType.APPLICATION_JSON)
                .content(dtoAsJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Validation did not pass")))
                .andExpect(jsonPath("$.object[0]", is("Token has expired")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void shouldReturnStatus200IfPasswordWasReset() throws Exception {
        final String dtoAsJson = objectMapper.writeValueAsString(new PasswordRecoveryDTO());
        when(passwordRecoveryService.resetPassword(any(PasswordRecoveryDTO.class))).thenReturn(new UserModel());
        
        mockMvc.perform(patch(PASSWORD_RECOVERY_MAPPING).contentType(MediaType.APPLICATION_JSON)
                .content(dtoAsJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Your password has been reset")))
                .andExpect(jsonPath("$.object", is(notNullValue())))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andExpect(status().isOk());
    }
    
}