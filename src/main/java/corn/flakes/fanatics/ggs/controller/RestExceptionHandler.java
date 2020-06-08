package corn.flakes.fanatics.ggs.controller;

import corn.flakes.fanatics.ggs.messages.MessageCode;
import corn.flakes.fanatics.ggs.messages.ResponseMessage;
import corn.flakes.fanatics.ggs.validator.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    
    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ResponseMessage<List<String>>> handleValidationException(ValidationException e) {
        log.warn(Arrays.toString(e.getStackTrace()));
        return ResponseEntity.badRequest()
                .body(new ResponseMessage<>(MessageCode.VALIDATION_DID_NOT_PASS.getMessage(), e.getMessages()));
    }
    
}
