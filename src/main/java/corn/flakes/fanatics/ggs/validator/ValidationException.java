package corn.flakes.fanatics.ggs.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Class representing exception when validation did not pass. Contains all validation messages from container
 *
 * @see corn.flakes.fanatics.ggs.messages.ValidationMessageContainer
 */
@RequiredArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
    
    private final List<String> messages;
    
}
