package corn.flakes.fanatics.ggs.messages;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationMessageContainer {
    
    private final List<String> messages;
    
    public ValidationMessageContainer(List<String> messages) {
        this.messages = messages;
    }
    
    public ValidationMessageContainer() {
        this.messages = new ArrayList<>();
    }
    
    public void addMessage(String message) {
        messages.add(message);
    }
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }
    
}
