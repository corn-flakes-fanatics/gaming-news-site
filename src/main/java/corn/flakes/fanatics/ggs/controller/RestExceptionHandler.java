package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.model.TokenNotFoundException;
import corn.flakes.fanatics.ggs.model.UserNotFoundException;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.internet.AddressException;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ResponseMessage<List<String>>> handleValidationException(ValidationException e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(new ResponseMessage<>(MessageCode.VALIDATION_DID_NOT_PASS.getMessage(), e.getMessages()));
    }
    
    @ExceptionHandler(value = { UserNotFoundException.class, TokenNotFoundException.class })
    public ResponseEntity<ResponseMessage<?>> handleUserNotFoundException(Exception e) {
        logException(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(e.getMessage()));
    }
    
    @ExceptionHandler(value = { AddressException.class, CannotSendEmailException.class })
    public ResponseEntity<ResponseMessage<?>> handleSendingEmailException(Exception e) {
        logException(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage<>(MessageCode.SERVER_SIDE_ERROR_OCCURRED.getMessage()));
    }
    
    private void logException(Exception e) {
        log.warn(Arrays.toString(e.getStackTrace()));
    }
    
}
