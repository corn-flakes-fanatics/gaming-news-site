package corn.flakes.fanatics.ggs.messages;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseMessage<T> {
    
    private final String message;
    
    private final T object;
    
    public ResponseMessage(String message) {
        this(message, null);
    }
    
}
