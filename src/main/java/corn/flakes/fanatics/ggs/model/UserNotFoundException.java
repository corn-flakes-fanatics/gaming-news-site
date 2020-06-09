package corn.flakes.fanatics.ggs.model;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
}
