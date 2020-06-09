package corn.flakes.fanatics.ggs.service;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.Collections;
import java.util.Map;

import static corn.flakes.fanatics.ggs.service.EmailConstants.*;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    
    private final it.ozimov.springboot.mail.service.EmailService emailService;
    
    @Override
    public Email sendPasswordRecoveryEmail(String token, String email) throws CannotSendEmailException, AddressException {
        final Email toSend = buildEmail(email);
        emailService.send(toSend, PASSWORD_RECOVERY_TEMPLATE, createModel(token));
        return toSend;
    }
    
    private Email buildEmail(String email) throws AddressException {
        return DefaultEmail.builder()
                .from(new InternetAddress(SENDER_ADDRESS))
                .to(Lists.newArrayList(new InternetAddress(email)))
                .subject(PASSWORD_RECOVERY_SUBJECT)
                .encoding(ENCODING)
                .body("")
                .build();
    }
    
    private Map<String, Object> createModel(String token) {
        return Collections.singletonMap("token", token);
    }
    
}
