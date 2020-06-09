package corn.flakes.fanatics.ggs.service;

import it.ozimov.springboot.mail.model.Email;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.mail.internet.InternetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EmailServiceImplTest {
    
    private it.ozimov.springboot.mail.service.EmailService emailSender;
    
    private EmailService emailService;
    
    @BeforeEach
    public void setUp() {
        emailSender = mock(it.ozimov.springboot.mail.service.EmailService.class);
        emailService = new EmailServiceImpl(emailSender);
    }
    
    @Test
    public void shouldSendEmailWithTokenToGivenAddress() throws Exception {
        final String token = "asds-1233-zxc1-123";
        final String email = "asdf@gmail.com";
        
        final Email result = emailService.sendPasswordRecoveryEmail(token, email);
        
        assertThat(result.getFrom()
                .getAddress(), is("noreply@ggs.com"));
        assertThat(result.getTo(), hasSize(1));
        assertThat(getToAddress(result.getTo()), is(email));
        assertThat(result.getSubject(), is("Password recovery"));
        assertThat(result.getEncoding(), is("UTF-8"));
        assertThat(result.getBody(), is(""));
        
        verify(emailSender, times(1)).send(result, "/emails/recovery.ftl", createModel(token));
    }
    
    @AfterEach
    public void tearDown() {
        emailSender = null;
        emailService = null;
    }
    
    private Map<String, Object> createModel(String token) {
        return Collections.singletonMap("token", token);
    }
    
    private String getToAddress(Collection<InternetAddress> addresses) {
        return addresses.stream()
                .findFirst()
                .get()
                .getAddress();
    }
    
}